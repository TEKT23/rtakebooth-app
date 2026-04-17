package usecase

import (
	"errors"

	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
)

type eventUsecase struct {
	repo domain.EventRepository
}

func NewEventUsecase(repo domain.EventRepository) domain.EventUsecase {
	return &eventUsecase{
		repo: repo,
	}
}

func (u *eventUsecase) CreateEvent(event *domain.Event) error {
	if event.Name == "" {
		return errors.New("event name is required")
	}

	// Set default status jika kosong
	if event.Status == "" {
		event.Status = "draft"
	}

	// Validasi status
	if !isValidStatus(event.Status) {
		return errors.New("invalid status: must be draft, active, or completed")
	}

	return u.repo.Create(event)
}

func (u *eventUsecase) GetAllEvents() ([]domain.Event, error) {
	return u.repo.GetAll()
}

func (u *eventUsecase) GetEventByID(id uint) (*domain.Event, error) {
	return u.repo.GetByID(id)
}

func (u *eventUsecase) UpdateEvent(event *domain.Event) error {
	if event.Name == "" {
		return errors.New("event name is required")
	}

	// Validasi status jika disediakan
	if event.Status != "" && !isValidStatus(event.Status) {
		return errors.New("invalid status: must be draft, active, or completed")
	}

	// Validasi event ada
	_, err := u.repo.GetByID(event.ID)
	if err != nil {
		return err
	}

	return u.repo.Update(event)
}

func (u *eventUsecase) DeleteEvent(id uint) error {
	// Validasi event ada
	_, err := u.repo.GetByID(id)
	if err != nil {
		return err
	}
	return u.repo.Delete(id)
}

// isValidStatus checks if the given status string is one of the allowed values.
func isValidStatus(status string) bool {
	validStatuses := map[string]bool{
		"draft":     true,
		"active":    true,
		"completed": true,
	}
	return validStatuses[status]
}
