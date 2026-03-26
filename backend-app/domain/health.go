package domain

type HealthResponse struct {
	Status  string `json:"status"`
	Message string `json:"message"`
}

type HealthUsecase interface {
	CheckHealth() (HealthResponse, error)
}
