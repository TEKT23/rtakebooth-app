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
	return r.db.Create(photo).Error
}

func (r *photoRepository) GetByID(id uint) (*domain.Photo, error) {
	var photo domain.Photo
	err := r.db.First(&photo, id).Error
	return &photo, err
}

func (r *photoRepository) GetBySessionID(sessionID uint) ([]domain.Photo, error) {
	var photos []domain.Photo
	err := r.db.Where("session_id = ?", sessionID).Find(&photos).Error
	return photos, err
}

func (r *photoRepository) Delete(id uint) error {
	return r.db.Delete(&domain.Photo{}, id).Error
}

