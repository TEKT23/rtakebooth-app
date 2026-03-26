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
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}
	c.JSON(http.StatusOK, res)
}
