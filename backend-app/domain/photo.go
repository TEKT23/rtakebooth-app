package domain

import (
	"context"
	"time"

	"gorm.io/gorm"
)

type Photo struct {
	ID        uint           `gorm:"primaryKey" json:"id"`
	SessionID uint           `gorm:"index" json:"session_id"`
	S3Url     string         `gorm:"type:text" json:"s3_url"`
	Type      string         `gorm:"type:varchar(20)" json:"type"` // raw, template, gif
	CreatedAt time.Time      `json:"created_at"`
	UpdatedAt time.Time      `json:"updated_at"`
	DeletedAt gorm.DeletedAt `gorm:"index" json:"-"`
}

type PhotoRepository interface {
	Save(photo *Photo) error
	GetByID(id uint) (*Photo, error)
	GetBySessionID(sessionID uint) ([]Photo, error)
	GetByEventID(eventID uint) ([]Photo, error)
	Delete(id uint) error
}

type PhotoUsecase interface {
	GetPhotosBySession(sessionID uint) ([]Photo, error)
	GetPhotosByEvent(eventID uint) ([]Photo, error)
	DeletePhoto(ctx context.Context, id uint) error
}

