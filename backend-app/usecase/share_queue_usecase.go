package usecase

import (
	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
)

type shareQueueUsecase struct {
	repo domain.ShareQueueRepository
}

func NewShareQueueUsecase(repo domain.ShareQueueRepository) domain.ShareQueueUsecase {
	return &shareQueueUsecase{
		repo: repo,
	}
}

func (u *shareQueueUsecase) GetSharingStatus() ([]domain.ShareStatusSummary, int64, error) {
	summaries, err := u.repo.GetStatusSummary()
	if err != nil {
		return nil, 0, err
	}

	var totalPending int64
	for _, s := range summaries {
		totalPending += s.Pending
	}

	return summaries, totalPending, nil
}

func (u *shareQueueUsecase) DeleteAllShares() error {
	return u.repo.DeleteAll()
}
