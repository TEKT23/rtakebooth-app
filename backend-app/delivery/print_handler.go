package delivery

import (
	"net/http"
	"strconv"

	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
	"github.com/gin-gonic/gin"
)

type PrintHandler struct {
	Usecase domain.PrintJobUsecase
}

func NewPrintHandler(r *gin.Engine, pu domain.PrintJobUsecase) {
	handler := &PrintHandler{
		Usecase: pu,
	}

	api := r.Group("/api/v1")
	{
		api.POST("/print/trigger/:photo_id", handler.TriggerPrint)
		api.GET("/print/history/:session_id", handler.GetPrintHistory)
	}
}

func (h *PrintHandler) TriggerPrint(c *gin.Context) {
	photoID, err := strconv.Atoi(c.Param("photo_id"))
	if err != nil {
		NewErrorResponse(c, http.StatusBadRequest, "invalid photo ID")
		return
	}

	var input struct {
		SessionID uint `json:"session_id" binding:"required"`
		Copies    int  `json:"copies"`
	}
	if err := c.ShouldBindJSON(&input); err != nil {
		NewErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	if input.Copies <= 0 {
		input.Copies = 1
	}

	job, err := h.Usecase.TriggerPrint(input.SessionID, uint(photoID), input.Copies)
	if err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	NewSuccessResponse(c, http.StatusOK, "Print triggered successfully", job)
}

func (h *PrintHandler) GetPrintHistory(c *gin.Context) {
	sessionID, err := strconv.Atoi(c.Param("session_id"))
	if err != nil {
		NewErrorResponse(c, http.StatusBadRequest, "invalid session ID")
		return
	}

	jobs, err := h.Usecase.GetPrintHistory(uint(sessionID))
	if err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	NewSuccessResponse(c, http.StatusOK, "Print history retrieved", jobs)
}
