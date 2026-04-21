package usecase

import (
	"context"
	"errors"
	"fmt"
	"io"
	"time"

	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
)

type sessionUsecase struct {
	repo           domain.SessionRepository
	photoRepo      domain.PhotoRepository
	eventRepo      domain.EventRepository
	paymentGateway domain.PaymentGateway
	storage        domain.StorageService
}

func NewSessionUsecase(repo domain.SessionRepository, photoRepo domain.PhotoRepository, eventRepo domain.EventRepository, pg domain.PaymentGateway, storage domain.StorageService) domain.SessionUsecase {
	return &sessionUsecase{
		repo:           repo,
		photoRepo:      photoRepo,
		eventRepo:      eventRepo,
		paymentGateway: pg,
		storage:        storage,
	}
}

func (u *sessionUsecase) CreateSession(eventID uint) (*domain.Session, error) {
	// 1. Verifikasi event aktif ada
	event, err := u.eventRepo.GetByID(eventID)
	if err != nil {
		return nil, fmt.Errorf("event not found: %w", err)
	}

	if event.IsActive != nil && !*event.IsActive {
		return nil, errors.New("event is not active")
	}

	// 2. Inisialisasi object Session
	session := &domain.Session{
		EventID:       eventID,
		StatusPayment: "pending",
	}

	// 3. Simpan dulu untuk dapat ID (atau bisa panggil payment dulu jika ID tidak wajib di QR saat init)
	err = u.repo.Create(session)
	if err != nil {
		return nil, err
	}

	// 4. Panggil Payment Gateway untuk dapat QR URL
	qrURL, err := u.paymentGateway.CreateTransactionQR(session.ID, 50000) // Dummy amount 50k
	if err != nil {
		return nil, err
	}

	// 5. Update session dengan QR URL
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

func (u *sessionUsecase) GetSessionGallery(sessionID uint) ([]domain.Photo, error) {
	// Validasi sesi ada
	_, err := u.repo.GetByID(sessionID)
	if err != nil {
		return nil, fmt.Errorf("session not found: %w", err)
	}

	return u.photoRepo.GetBySessionID(sessionID)
}
