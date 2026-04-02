package repository

import (
	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
	"gorm.io/gorm"
)

type settingRepository struct {
	db *gorm.DB
}

func NewSettingRepository(db *gorm.DB) domain.SettingRepository {
	return &settingRepository{
		db: db,
	}
}

func (r *settingRepository) GetByCategory(category string) (*domain.Setting, error) {
	var setting domain.Setting
	err := r.db.Where("category = ?", category).First(&setting).Error
	if err != nil {
		return nil, err
	}
	return &setting, nil
}

func (r *settingRepository) Upsert(setting *domain.Setting) error {
	var existing domain.Setting
	err := r.db.Where("category = ?", setting.Category).First(&existing).Error
	if err == nil {
		// Update existing
		setting.ID = existing.ID
		return r.db.Save(setting).Error
	}
	// Create new
	return r.db.Create(setting).Error
}
