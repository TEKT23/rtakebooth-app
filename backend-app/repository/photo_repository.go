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

func (r *photoRepository) GetBySessionID(sessionID uint) ([]domain.Photo, error) {
	var photos []domain.Photo
	err := r.db.Where("session_id = ?", sessionID).Find(&photos).Error
	return photos, err
}
