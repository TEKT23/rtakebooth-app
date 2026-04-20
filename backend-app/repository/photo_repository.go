package repository

import (
	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
	"gorm.io/gorm"
)

type photoRepository struct {
	db *gorm.DB
}

func NewPhotoRepository(db *gorm.DB) domain.PhotoRepository {
	return &photoRepository{
		db: db,
	}
}

func (r *photoRepository) Save(photo *domain.Photo) error {
	return r.db.Save(photo).Error
}

func (r *photoRepository) GetByID(id uint) (*domain.Photo, error) {
	var photo domain.Photo
	err := r.db.First(&photo, id).Error
	return &photo, err
}

func (r *photoRepository) GetBySessionID(sessionID uint) ([]domain.Photo, error) {
	var photos []domain.Photo
	err := r.db.Where("session_id = ?", sessionID).Order("created_at ASC").Find(&photos).Error
	return photos, err
}

func (r *photoRepository) GetByEventID(eventID uint) ([]domain.Photo, error) {
	var photos []domain.Photo
	// Join with sessions table to filter by event_id
	err := r.db.Joins("JOIN sessions ON sessions.id = photos.session_id").
		Where("sessions.event_id = ?", eventID).
		Order("photos.created_at ASC").
		Find(&photos).Error
	return photos, err
}

func (r *photoRepository) Delete(id uint) error {
	return r.db.Delete(&domain.Photo{}, id).Error
}
