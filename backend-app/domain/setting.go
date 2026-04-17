package domain

import (
	"encoding/json"
	"time"
)

type Setting struct {
	ID        uint            `gorm:"primaryKey" json:"id"`
	Category  string          `gorm:"type:varchar(50);uniqueIndex;not null" json:"category"`
	Payload   json.RawMessage `gorm:"type:json" json:"payload"`
	CreatedAt time.Time       `json:"created_at"`
	UpdatedAt time.Time       `json:"updated_at"`
}

type SettingRepository interface {
	GetByCategory(category string) (*Setting, error)
	Upsert(setting *Setting) error
}

type SettingUsecase interface {
	GetSettings(category string) (map[string]interface{}, error)
	SaveSettings(category string, payload map[string]interface{}) error
}
