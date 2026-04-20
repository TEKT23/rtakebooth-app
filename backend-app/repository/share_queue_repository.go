package repository

import (
	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
	"gorm.io/gorm"
)

type shareQueueRepository struct {
	db *gorm.DB
}

func NewShareQueueRepository(db *gorm.DB) domain.ShareQueueRepository {
	return &shareQueueRepository{
		db: db,
	}
}

func (r *shareQueueRepository) GetStatusSummary() ([]domain.ShareStatusSummary, error) {
	var summaries []domain.ShareStatusSummary

	// Aggregate counts per channel
	err := r.db.Model(&domain.ShareQueue{}).
		Select(`channel, 
			SUM(CASE WHEN status = 'sent' THEN 1 ELSE 0 END) as sent, 
			SUM(CASE WHEN status = 'pending' THEN 1 ELSE 0 END) as pending`).
		Group("channel").
		Scan(&summaries).Error

	if err != nil {
		return nil, err
	}

	// If no data, return default empty channels
	if len(summaries) == 0 {
		summaries = []domain.ShareStatusSummary{
			{Channel: "Email", Sent: 0, Pending: 0},
			{Channel: "SMS", Sent: 0, Pending: 0},
			{Channel: "Print", Sent: 0, Pending: 0},
			{Channel: "Upload (Cloud)", Sent: 0, Pending: 0},
			{Channel: "WhatsApp", Sent: 0, Pending: 0},
		}
	}

	return summaries, nil
}

func (r *shareQueueRepository) DeleteAll() error {
	return r.db.Where("1 = 1").Delete(&domain.ShareQueue{}).Error
}
