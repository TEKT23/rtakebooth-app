package usecase

import (
	"archive/zip"
	"fmt"
	"io"
	"net/http"
	"os"
	"path/filepath"

	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
)

type exportUsecase struct {
	eventRepo domain.EventRepository
	photoRepo domain.PhotoRepository
}

func NewExportUsecase(eventRepo domain.EventRepository, photoRepo domain.PhotoRepository) domain.ExportUsecase {
	return &exportUsecase{
		eventRepo: eventRepo,
		photoRepo: photoRepo,
	}
}

func (u *exportUsecase) ExportEventPhotos(eventID uint) (string, error) {
	// 1. Get Event to verify
	event, err := u.eventRepo.GetByID(eventID)
	if err != nil {
		return "", fmt.Errorf("event not found: %w", err)
	}

	// 2. Get all photos for this event
	photos, err := u.photoRepo.GetByEventID(eventID)
	if err != nil {
		return "", fmt.Errorf("failed to get event photos: %w", err)
	}

	if len(photos) == 0 {
		return "", fmt.Errorf("no photos found for this event")
	}

	// 3. Create temporary ZIP file
	tmpDir := os.TempDir()
	zipFileName := fmt.Sprintf("export_event_%d_%s.zip", eventID, event.Name)
	zipPath := filepath.Join(tmpDir, zipFileName)

	zipFile, err := os.Create(zipPath)
	if err != nil {
		return "", fmt.Errorf("failed to create zip file: %w", err)
	}
	defer zipFile.Close()

	archive := zip.NewWriter(zipFile)
	defer archive.Close()

	// 4. Download and add each photo to ZIP
	for _, photo := range photos {
		err := u.addPhotoToZip(archive, photo)
		if err != nil {
			fmt.Printf("Warning: failed to add photo %d to zip: %v\n", photo.ID, err)
			continue
		}
	}

	return zipPath, nil
}

func (u *exportUsecase) addPhotoToZip(archive *zip.Writer, photo domain.Photo) error {
	resp, err := http.Get(photo.S3Url)
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return fmt.Errorf("bad status: %s", resp.Status)
	}

	// Use original filename or generate one
	fileName := filepath.Base(photo.S3Url)
	if fileName == "" || fileName == "." {
		fileName = fmt.Sprintf("photo_%d.jpg", photo.ID)
	}

	writer, err := archive.Create(fmt.Sprintf("sessions/%d/%s", photo.SessionID, fileName))
	if err != nil {
		return err
	}

	_, err = io.Copy(writer, resp.Body)
	return err
}
