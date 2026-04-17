package delivery

import (
	"net/http"

	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
	"github.com/gin-gonic/gin"
)

type ShareQueueHandler struct {
	ShareQueueUsecase domain.ShareQueueUsecase
}

func NewShareQueueHandler(r *gin.Engine, squ domain.ShareQueueUsecase) {
	handler := &ShareQueueHandler{
		ShareQueueUsecase: squ,
	}

	api := r.Group("/api/v1")
	{
		api.GET("/sharing/status", handler.GetSharingStatus)
		api.DELETE("/sharing/queue", handler.DeleteAllShares)
	}
}

func (h *ShareQueueHandler) GetSharingStatus(c *gin.Context) {
	summaries, totalPending, err := h.ShareQueueUsecase.GetSharingStatus()
	if err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	// Match frontend's expected structure
	syncStatus := "UP_TO_DATE"
	if totalPending > 0 {
		syncStatus = "SYNCING"
	}

	NewSuccessResponse(c, http.StatusOK, "Sharing status retrieved", gin.H{
		"total_pending": totalPending,
		"sync_status":   syncStatus,
		"queues":        summaries,
	})
}

func (h *ShareQueueHandler) DeleteAllShares(c *gin.Context) {
	err := h.ShareQueueUsecase.DeleteAllShares()
	if err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	NewSuccessResponse(c, http.StatusOK, "All shares deleted", nil)
}
