package usecase

import (
	"context"
	"fmt"

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

func (u *photoUsecase) GetPhotosBySession(sessionID uint) ([]domain.Photo, error) {
	return u.photoRepo.GetBySessionID(sessionID)
}

func (u *photoUsecase) GetPhotosByEvent(eventID uint) ([]domain.Photo, error) {
	return u.photoRepo.GetByEventID(eventID)
}

func (u *photoUsecase) DeletePhoto(ctx context.Context, id uint) error {
	// 1. Get photo to retrieve S3 URL
	photo, err := u.photoRepo.GetByID(id)
	if err != nil {
		return fmt.Errorf("photo not found: %w", err)
	}

	// 2. Delete from S3
	s3Key, err := storage.ExtractS3Key(photo.S3Url)
	if err == nil && s3Key != "" {
		_ = u.storage.DeleteFile(ctx, s3Key) // Best effort
	}

	// 3. Delete from database
	return u.photoRepo.Delete(id)
}
