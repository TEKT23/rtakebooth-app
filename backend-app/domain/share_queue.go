package domain

import "time"

type ShareQueue struct {
	ID        uint      `gorm:"primaryKey" json:"id"`
	SessionID uint      `gorm:"index" json:"session_id"`
	Channel   string    `gorm:"type:varchar(20)" json:"channel"` // email, sms, print, upload, whatsapp
	Status    string    `gorm:"type:varchar(20);default:'pending'" json:"status"` // pending, sent, failed
	CreatedAt time.Time `json:"created_at"`
	UpdatedAt time.Time `json:"updated_at"`
}

// ShareStatusSummary is the aggregated view per channel for the dashboard.
type ShareStatusSummary struct {
	Channel string `json:"channel"`
	Sent    int64  `json:"sent"`
	Pending int64  `json:"pending"`
}

type ShareQueueRepository interface {
	GetStatusSummary() ([]ShareStatusSummary, error)
	DeleteAll() error
}

type ShareQueueUsecase interface {
	GetSharingStatus() ([]ShareStatusSummary, int64, error) // summaries, totalPending, error
	DeleteAllShares() error
}
