package delivery

import (
	"net/http"

	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
	"github.com/gin-gonic/gin"
)

type SessionHandler struct {
	SessionUsecase domain.SessionUsecase
}

func NewSessionHandler(r *gin.Engine, su domain.SessionUsecase) {
	handler := &SessionHandler{
		SessionUsecase: su,
	}

	api := r.Group("/api/v1")
	{
		api.POST("/sessions", handler.CreateSession)
		api.GET("/sessions/:id/status", handler.GetSessionStatus)
		api.POST("/internal/webhook/sessions/:id/paid", handler.SetPaymentPaid)
	}
}

func (h *SessionHandler) CreateSession(c *gin.Context) {
	var input struct {
		EventID uint `json:"event_id" binding:"required"`
	}
	if err := c.ShouldBindJSON(&input); err != nil {
		NewErrorResponse(c, http.StatusBadRequest, "event_id is required")
		return
	}

	session, err := h.SessionUsecase.CreateSession(input.EventID)
	if err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	NewSuccessResponse(c, http.StatusCreated, "Session created successfully", session)
}

func (h *SessionHandler) GetSessionStatus(c *gin.Context) {
	var id struct {
		ID uint `uri:"id" binding:"required"`
	}
	if err := c.ShouldBindUri(&id); err != nil {
		NewErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	status, err := h.SessionUsecase.GetSessionStatus(id.ID)
	if err != nil {
		NewErrorResponse(c, http.StatusNotFound, "Session not found")
		return
	}

	NewSuccessResponse(c, http.StatusOK, "Session status retrieved", gin.H{
		"session_id": id.ID,
		"status":     status,
	})
}

func (h *SessionHandler) SetPaymentPaid(c *gin.Context) {
	var id struct {
		ID uint `uri:"id" binding:"required"`
	}
	if err := c.ShouldBindUri(&id); err != nil {
		NewErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	err := h.SessionUsecase.SetPaymentPaid(id.ID)
	if err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	NewSuccessResponse(c, http.StatusOK, "Payment status updated to PAID", nil)
}
