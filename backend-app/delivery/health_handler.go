package delivery

import (
	"net/http"

	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
	"github.com/gin-gonic/gin"
)

type HealthHandler struct {
	HealthUsecase domain.HealthUsecase
}

func NewHealthHandler(r *gin.Engine, hu domain.HealthUsecase) {
	handler := &HealthHandler{
		HealthUsecase: hu,
	}

	r.GET("/health", handler.CheckHealth)
}

func (h *HealthHandler) CheckHealth(c *gin.Context) {
	res, err := h.HealthUsecase.CheckHealth()
	if err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}
	NewSuccessResponse(c, http.StatusOK, "Health check success", res)
}
