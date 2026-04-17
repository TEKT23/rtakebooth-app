package usecase

import (
	"encoding/json"
	"errors"

	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
	"gorm.io/gorm"
)

type settingUsecase struct {
	repo domain.SettingRepository
}

func NewSettingUsecase(repo domain.SettingRepository) domain.SettingUsecase {
	return &settingUsecase{
		repo: repo,
	}
}

func (u *settingUsecase) GetSettings(category string) (map[string]interface{}, error) {
	setting, err := u.repo.GetByCategory(category)
	if err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			return make(map[string]interface{}), nil
		}
		return nil, err
	}

	var payload map[string]interface{}
	err = json.Unmarshal(setting.Payload, &payload)
	if err != nil {
		return nil, err
	}

	return payload, nil
}

func (u *settingUsecase) SaveSettings(category string, payload map[string]interface{}) error {
	// Validasi kategori yang diizinkan (optional but recommended)
	validCategories := map[string]bool{
		"general":   true,
		"print":     true,
		"sharing":   true,
		"capture":   true,
		"camera":    true,
		"attendant": true,
		"layout":    true,
	}

	if !validCategories[category] {
		return errors.New("invalid settings category")
	}

	payloadBytes, err := json.Marshal(payload)
	if err != nil {
		return err
	}

	setting := &domain.Setting{
		Category: category,
		Payload:  json.RawMessage(payloadBytes),
	}

	return u.repo.Upsert(setting)
}
