package delivery

import (
	"net/http"

	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
	"github.com/gin-gonic/gin"
)

type SettingHandler struct {
	SettingUsecase domain.SettingUsecase
}

func NewSettingHandler(r *gin.Engine, su domain.SettingUsecase) {
	handler := &SettingHandler{
		SettingUsecase: su,
	}

	api := r.Group("/api/v1")
	{
		api.GET("/settings/:category", handler.GetSettings)
		api.PUT("/settings/:category", handler.SaveSettings)
	}
}

func (h *SettingHandler) GetSettings(c *gin.Context) {
	category := c.Param("category")
	settings, err := h.SettingUsecase.GetSettings(category)
	if err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	NewSuccessResponse(c, http.StatusOK, "Settings retrieved successfully", settings)
}

func (h *SettingHandler) SaveSettings(c *gin.Context) {
	category := c.Param("category")
	var payload map[string]interface{}
	if err := c.ShouldBindJSON(&payload); err != nil {
		NewErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	if err := h.SettingUsecase.SaveSettings(category, payload); err != nil {
		NewErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	NewSuccessResponse(c, http.StatusOK, "Settings saved successfully", payload)
}
