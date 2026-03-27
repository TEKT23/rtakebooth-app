package usecase

import (
	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
)

type SessionUsecase interface {
	CreateSession() (*domain.Session, error)
}

type sessionUsecase struct {
	repo           domain.SessionRepository
	paymentGateway domain.PaymentGateway
}

func NewSessionUsecase(repo domain.SessionRepository, pg domain.PaymentGateway) SessionUsecase {
	return &sessionUsecase{
		repo:           repo,
		paymentGateway: pg,
	}
}

func (u *sessionUsecase) CreateSession() (*domain.Session, error) {
	// 1. Inisialisasi object Session
	session := &domain.Session{
		StatusPayment: "pending",
	}

	// 2. Simpan dulu untuk dapat ID (atau bisa panggil payment dulu jika ID tidak wajib di QR saat init)
	// Namun di mock kita butuh ID untuk URL QR dummy.
	err := u.repo.Create(session)
	if err != nil {
		return nil, err
	}

	// 3. Panggil Payment Gateway untuk dapat QR URL
	qrURL, err := u.paymentGateway.CreateTransactionQR(session.ID, 50000) // Dummy amount 50k
	if err != nil {
		return nil, err
	}

	// 4. Update session dengan QR URL
	session.QRUrl = qrURL
	err = u.repo.Update(session)
	if err != nil {
		return nil, err
	}

	return session, nil
}
