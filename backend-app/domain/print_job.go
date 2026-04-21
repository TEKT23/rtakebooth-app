package domain

import "time"

type PrintJob struct {
	ID        uint      `gorm:"primaryKey" json:"id"`
	SessionID uint      `gorm:"index" json:"session_id"`
	PhotoID   uint      `json:"photo_id"`
	Status    string    `gorm:"type:varchar(20);default:'pending'" json:"status"` // pending, processing, success, failed
	Copies    int       `gorm:"default:1" json:"copies"`
	CreatedAt time.Time `json:"created_at"`
	UpdatedAt time.Time `json:"updated_at"`
}

type PrintJobRepository interface {
	Create(job *PrintJob) error
	GetByID(id uint) (*PrintJob, error)
	GetBySessionID(sessionID uint) ([]PrintJob, error)
	Update(job *PrintJob) error
}

type PrintJobUsecase interface {
	TriggerPrint(sessionID uint, photoID uint, copies int) (*PrintJob, error)
	GetPrintHistory(sessionID uint) ([]PrintJob, error)
}

type PrinterService interface {
	PrintFile(filePath string, printerName string, copies int) error
}
