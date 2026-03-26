package usecase

import (
	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
)

type healthUsecase struct{}

func NewHealthUsecase() domain.HealthUsecase {
	return &healthUsecase{}
}

func (u *healthUsecase) CheckHealth() (domain.HealthResponse, error) {
	return domain.HealthResponse{
		Status:  "UP",
		Message: "Backend is running smoothly",
	}, nil
}
