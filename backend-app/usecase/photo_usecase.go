package usecase

import (
	"context"
	"fmt"
	"io"
	"time"

	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
	"github.com/TEKT23/rtakebooth-app/backend-app/infrastructure/storage"
)

type photoUsecase struct {
	photoRepo   domain.PhotoRepository
	sessionRepo domain.SessionRepository
	storage     domain.StorageService
}

func NewPhotoUsecase(photoRepo domain.PhotoRepository, sessionRepo domain.SessionRepository, storageService domain.StorageService) domain.PhotoUsecase {
	return &photoUsecase{
		photoRepo:   photoRepo,
		sessionRepo: sessionRepo,
		storage:     storageService,
	}
}

func (u *photoUsecase) UploadPhoto(ctx context.Context, sessionID uint, file io.Reader, fileType string) (*domain.Photo, error) {
	// 1. Verifikasi sesi ada
	_, err := u.sessionRepo.GetByID(sessionID)
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

func (u *photoUsecase) GetPhotosBySession(sessionID uint) ([]domain.Photo, error) {
	// Verifikasi sesi ada
	_, err := u.sessionRepo.GetByID(sessionID)
	if err != nil {
		return nil, fmt.Errorf("session not found: %w", err)
	}
	return u.photoRepo.GetBySessionID(sessionID)
}

func (u *photoUsecase) GetPhotosByEvent(eventID uint) ([]domain.Photo, error) {
	return u.photoRepo.GetByEventID(eventID)
}

func (u *photoUsecase) DeletePhoto(ctx context.Context, id uint) error {
	// 1. Ambil photo untuk dapat S3 URL
	photo, err := u.photoRepo.GetByID(id)
	if err != nil {
		return fmt.Errorf("photo not found: %w", err)
	}

	// 2. Hapus dari S3
	s3Key, err := storage.ExtractS3Key(photo.S3Url)
	if err == nil && s3Key != "" {
		_ = u.storage.DeleteFile(ctx, s3Key) // Best effort delete from S3
	}

	// 3. Hapus dari database
	return u.photoRepo.Delete(id)
}
func (u *photoUsecase) GetPresignedURL(ctx context.Context, id uint) (string, error) {
	photo, err := u.photoRepo.GetByID(id)
	if err != nil {
		return "", fmt.Errorf("photo not found: %w", err)
	}

	s3Key, err := storage.ExtractS3Key(photo.S3Url)
	if err != nil {
		return "", fmt.Errorf("invalid S3 URL: %w", err)
	}

	return u.storage.GetPresignedURL(ctx, s3Key)
}
