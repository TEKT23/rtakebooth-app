package domain

type PaymentGateway interface {
	CreateTransactionQR(sessionID uint, amount int64) (string, error)
	CheckTransactionStatus(externalID string) (string, error)
}
