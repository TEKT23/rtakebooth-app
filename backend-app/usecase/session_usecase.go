package usecase

import (
	"context"
	"fmt"
	"io"
	"time"

	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
	"github.com/TEKT23/rtakebooth-app/backend-app/repository"
)

type SessionUsecase interface {
	CreateSession() (*domain.Session, error)
	SetPaymentPaid(id uint) error
	GetSessionStatus(id uint) (string, error)
	UploadSessionPhoto(ctx context.Context, sessionID uint, file io.Reader, fileType string) (*domain.Photo, error)
}

type sessionUsecase struct {
	repo           domain.SessionRepository
	photoRepo      repository.PhotoRepository
	paymentGateway domain.PaymentGateway
	storage        domain.StorageService
}

func NewSessionUsecase(repo domain.SessionRepository, photoRepo repository.PhotoRepository, pg domain.PaymentGateway, storage domain.StorageService) SessionUsecase {
	return &sessionUsecase{
		repo:           repo,
		photoRepo:      photoRepo,
		paymentGateway: pg,
		storage:        storage,
	}
}

func (u *sessionUsecase) CreateSession() (*domain.Session, error) {
	// 1. Inisialisasi object Session
	session := &domain.Session{
		StatusPayment: "pending",
	}

	// 2. Simpan dulu untuk dapat ID (atau bisa panggil payment dulu jika ID tidak wajib di QR saat init)
	// Namun di mock kita butuh ID untuk URL QR dummy.
	err := u.repo.Create(session)
	if err != nil {
		return nil, err
	}

	// 3. Panggil Payment Gateway untuk dapat QR URL
	qrURL, err := u.paymentGateway.CreateTransactionQR(session.ID, 50000) // Dummy amount 50k
	if err != nil {
		return nil, err
	}

	// 4. Update session dengan QR URL
	session.QRUrl = qrURL
	err = u.repo.Update(session)
	if err != nil {
		return nil, err
	}

	return session, nil
}

func (u *sessionUsecase) SetPaymentPaid(id uint) error {
	session, err := u.repo.GetByID(id)
	if err != nil {
		return err
	}

	session.StatusPayment = "PAID"
	return u.repo.Update(session)
}

func (u *sessionUsecase) GetSessionStatus(id uint) (string, error) {
	session, err := u.repo.GetByID(id)
	if err != nil {
		return "", err
	}
	return session.StatusPayment, nil
}

func (u *sessionUsecase) UploadSessionPhoto(ctx context.Context, sessionID uint, file io.Reader, fileType string) (*domain.Photo, error) {
	// 1. Verifikasi sesi ada
	_, err := u.repo.GetByID(sessionID)
	if err != nil {
		return nil, fmt.Errorf("session not found: %w", err)
	}

	// 2. Tentukan nama file unik
	fileName := fmt.Sprintf("sessions/%d/%d_%s.jpg", sessionID, time.Now().UnixNano(), fileType)

	// 3. Upload ke S3
	s3URL, err := u.storage.UploadFile(ctx, file, fileName, "image/jpeg")
	if err != nil {
		return nil, err
	}

	// 4. Simpan ke database
	photo := &domain.Photo{
		SessionID: sessionID,
		S3Url:     s3URL,
		Type:      fileType,
	}

	err = u.photoRepo.Save(photo)
	if err != nil {
		return nil, err
	}

	return photo, nil
}
