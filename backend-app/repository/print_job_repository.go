package repository

import (
	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
	"gorm.io/gorm"
)

type printJobRepository struct {
	db *gorm.DB
}

func NewPrintJobRepository(db *gorm.DB) domain.PrintJobRepository {
	return &printJobRepository{
		db: db,
	}
}

func (r *printJobRepository) Create(job *domain.PrintJob) error {
	return r.db.Create(job).Error
}

func (r *printJobRepository) GetByID(id uint) (*domain.PrintJob, error) {
	var job domain.PrintJob
	err := r.db.First(&job, id).Error
	return &job, err
}

func (r *printJobRepository) GetBySessionID(sessionID uint) ([]domain.PrintJob, error) {
	var jobs []domain.PrintJob
	err := r.db.Where("session_id = ?", sessionID).Order("created_at DESC").Find(&jobs).Error
	return jobs, err
}

func (r *printJobRepository) Update(job *domain.PrintJob) error {
	return r.db.Save(job).Error
}
