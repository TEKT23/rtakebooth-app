package repository

import (
	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
	"gorm.io/gorm"
)

type eventRepository struct {
	db *gorm.DB
}

func NewEventRepository(db *gorm.DB) domain.EventRepository {
	return &eventRepository{
		db: db,
	}
}

func (r *eventRepository) Create(event *domain.Event) error {
	return r.db.Create(event).Error
}

func (r *eventRepository) GetAll() ([]domain.Event, error) {
	var events []domain.Event
	err := r.db.Find(&events).Error
	return events, err
}

func (r *eventRepository) GetByID(id uint) (*domain.Event, error) {
	var event domain.Event
	err := r.db.Preload("Sessions").First(&event, id).Error
	return &event, err
}

func (r *eventRepository) Update(event *domain.Event) error {
	return r.db.Save(event).Error
}

func (r *eventRepository) Delete(id uint) error {
	return r.db.Delete(&domain.Event{}, id).Error
}
