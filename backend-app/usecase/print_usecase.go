package usecase

import (
	"fmt"
	"io"
	"net/http"
	"os"
	"path/filepath"

	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
)

type printJobUsecase struct {
	repo         domain.PrintJobRepository
	photoRepo    domain.PhotoRepository
	settingsRepo domain.SettingRepository
	printer      domain.PrinterService
}

func NewPrintJobUsecase(repo domain.PrintJobRepository, photoRepo domain.PhotoRepository, settingsRepo domain.SettingRepository, printer domain.PrinterService) domain.PrintJobUsecase {
	return &printJobUsecase{
		repo:         repo,
		photoRepo:    photoRepo,
		settingsRepo: settingsRepo,
		printer:      printer,
	}
}

func (u *printJobUsecase) TriggerPrint(sessionID uint, photoID uint, copies int) (*domain.PrintJob, error) {
	// 1. Get Photo
	photo, err := u.photoRepo.GetByID(photoID)
	if err != nil {
		return nil, fmt.Errorf("photo not found: %w", err)
	}

	// 2. Create Job Record
	job := &domain.PrintJob{
		SessionID: sessionID,
		PhotoID:   photoID,
		Copies:    copies,
		Status:    "processing",
	}
	u.repo.Create(job)

	// 3. Download file from S3 to temporary local path
	tmpDir := os.TempDir()
	localPath := filepath.Join(tmpDir, fmt.Sprintf("print_%d.jpg", photoID))
	
	err = u.downloadFile(photo.S3Url, localPath)
	if err != nil {
		job.Status = "failed"
		u.repo.Update(job)
		return job, fmt.Errorf("failed to download photo: %w", err)
	}
	defer os.Remove(localPath)

	// 4. Get Printer Name from Settings
	printerName := ""
	_, err = u.settingsRepo.GetByCategory("print")
	if err == nil {
		// Try to parse printer name from payload if it exists
		// For now we keep it empty to use default
	}

	// 5. Trigger physical print
	err = u.printer.PrintFile(localPath, printerName, copies)
	if err != nil {
		job.Status = "failed"
		u.repo.Update(job)
		return job, fmt.Errorf("print service failed: %w", err)
	}

	// 6. Success
	job.Status = "success"
	u.repo.Update(job)

	return job, nil
}

func (u *printJobUsecase) GetPrintHistory(sessionID uint) ([]domain.PrintJob, error) {
	return u.repo.GetBySessionID(sessionID)
}

func (u *printJobUsecase) downloadFile(url string, destPath string) error {
	resp, err := http.Get(url)
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return fmt.Errorf("bad status: %s", resp.Status)
	}

	out, err := os.Create(destPath)
	if err != nil {
		return err
	}
	defer out.Close()

	_, err = io.Copy(out, resp.Body)
	return err
}
