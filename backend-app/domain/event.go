package domain

import (
	"time"

	"gorm.io/gorm"
)

type Event struct {
	ID        uint           `gorm:"primaryKey" json:"id"`
	Name      string         `gorm:"type:varchar(255);not null" json:"name"`
	Date      time.Time      `json:"date"`
	Location  string         `gorm:"type:text" json:"location"`
	IsActive  bool           `gorm:"default:true" json:"is_active"`
	CreatedAt time.Time      `json:"created_at"`
	UpdatedAt time.Time      `json:"updated_at"`
	DeletedAt gorm.DeletedAt `gorm:"index" json:"-"`
	Sessions  []Session      `gorm:"foreignKey:EventID" json:"sessions,omitempty"`
}

type EventRepository interface {
	Create(event *Event) error
	GetAll() ([]Event, error)
	GetByID(id uint) (*Event, error)
	Update(event *Event) error
	Delete(id uint) error
}

type EventUsecase interface {
	CreateEvent(event *Event) error
	GetAllEvents() ([]Event, error)
	GetEventByID(id uint) (*Event, error)
	UpdateEvent(event *Event) error
	DeleteEvent(id uint) error
}
