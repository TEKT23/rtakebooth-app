package repository

import (
	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
	"gorm.io/gorm"
)

type sessionRepository struct {
	db *gorm.DB
}

func NewSessionRepository(db *gorm.DB) domain.SessionRepository {
	return &sessionRepository{
		db: db,
	}
}

func (r *sessionRepository) Create(session *domain.Session) error {
	return r.db.Create(session).Error
}

func (r *sessionRepository) Update(session *domain.Session) error {
	return r.db.Save(session).Error
}

func (r *sessionRepository) GetByID(id uint) (*domain.Session, error) {
	var session domain.Session
	err := r.db.First(&session, id).Error
	return &session, err
}
