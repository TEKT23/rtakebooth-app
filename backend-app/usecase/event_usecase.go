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
