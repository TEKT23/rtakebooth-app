package payment

import (
	"fmt"

	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
)

type mockPaymentProvider struct{}

func NewMockPaymentProvider() domain.PaymentGateway {
	return &mockPaymentProvider{}
}

func (m *mockPaymentProvider) CreateTransactionQR(sessionID uint, amount int64) (string, error) {
	// Dummy QR URL
	qrURL := fmt.Sprintf("https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=session_%d", sessionID)
	return qrURL, nil
}

func (m *mockPaymentProvider) CheckTransactionStatus(externalID string) (string, error) {
	return "PENDING", nil
}
