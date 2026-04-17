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
	if err != nil {
		return nil, err
	}

	// Compute photo counts for each event
	for i := range events {
		r.populateCounts(&events[i])
	}

	return events, nil
}

func (r *eventRepository) GetByID(id uint) (*domain.Event, error) {
	var event domain.Event
	err := r.db.Preload("Sessions").First(&event, id).Error
	if err != nil {
		return nil, err
	}

	// Compute photo count
	r.populateCounts(&event)

	return &event, err
}

func (r *eventRepository) Update(event *domain.Event) error {
	return r.db.Save(event).Error
}

func (r *eventRepository) Delete(id uint) error {
	return r.db.Delete(&domain.Event{}, id).Error
}

// populateCounts computes PhotoCount by counting photos across all sessions of this event.
// PrintCount is set to 0 for now (PrintJob entity not yet implemented).
func (r *eventRepository) populateCounts(event *domain.Event) {
	var photoCount int64
	r.db.Model(&domain.Photo{}).
		Joins("JOIN sessions ON sessions.id = photos.session_id").
		Where("sessions.event_id = ? AND sessions.deleted_at IS NULL AND photos.deleted_at IS NULL", event.ID).
		Count(&photoCount)
	event.PhotoCount = photoCount

	// PrintCount will be populated once PrintJob entity is created
	event.PrintCount = 0
}
