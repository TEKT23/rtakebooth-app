package domain

import (
	"context"
	"io"
	"time"

	"gorm.io/gorm"
)

type Session struct {
	ID            uint           `gorm:"primaryKey" json:"id"`
	EventID       uint           `gorm:"index" json:"event_id"`
	StatusPayment string         `gorm:"type:varchar(20);default:'pending'" json:"status_payment"`
	QRUrl         string         `gorm:"type:text" json:"qr_url"`
	CreatedAt     time.Time      `json:"created_at"`
	UpdatedAt     time.Time      `json:"updated_at"`
	DeletedAt     gorm.DeletedAt `gorm:"index" json:"-"`
	Photos        []Photo        `gorm:"foreignKey:SessionID" json:"photos,omitempty"`
}

type SessionRepository interface {
	Create(session *Session) error
	Update(session *Session) error
	GetByID(id uint) (*Session, error)
}

type SessionUsecase interface {
	CreateSession() (*Session, error)
	SetPaymentPaid(id uint) error
	GetSessionStatus(id uint) (string, error)
	UploadSessionPhoto(ctx context.Context, sessionID uint, file io.Reader, fileType string) (*Photo, error)
	GetSessionGallery(sessionID uint) ([]Photo, error)
}

