package repository

import (
	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
	"gorm.io/gorm"
)

type PhotoRepository interface {
	Save(photo *domain.Photo) error
}

type photoRepository struct {
	db *gorm.DB
}

func NewPhotoRepository(db *gorm.DB) PhotoRepository {
	return &photoRepository{
		db: db,
	}
}

func (r *photoRepository) Save(photo *domain.Photo) error {
	return r.db.Save(photo).Error
}
