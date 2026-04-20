package domain

import (
	"time"

	"gorm.io/gorm"
)

type Event struct {
	ID          uint           `gorm:"primaryKey" json:"id"`
	Name        string         `gorm:"type:varchar(255);not null" json:"name"`
	Date        time.Time      `json:"date"`
	Location    string         `gorm:"type:varchar(255)" json:"location"`
	Description string         `gorm:"type:text" json:"description"`
	Status      string         `gorm:"type:varchar(20);default:'draft'" json:"status"` // draft, active, completed
	CreatedAt   time.Time      `json:"created_at"`
	UpdatedAt   time.Time      `json:"updated_at"`
	DeletedAt   gorm.DeletedAt `gorm:"index" json:"-"`
	Sessions    []Session      `gorm:"foreignKey:EventID" json:"sessions,omitempty"`
}

type EventRepository interface {
	Create(event *Event) error
	GetByID(id uint) (*Event, error)
	GetAll() ([]Event, error)
	Update(event *Event) error
	Delete(id uint) error
}
