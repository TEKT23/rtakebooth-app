package delivery

import (
	"fmt"
	"net/http"
	"os"
	"strconv"

	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
	"github.com/gin-gonic/gin"
)

type ExportHandler struct {
	Usecase domain.ExportUsecase
}

func NewExportHandler(r *gin.Engine, eu domain.ExportUsecase) {
	handler := &ExportHandler{
		Usecase: eu,
	}

	api := r.Group("/api/v1")
	{
		api.GET("/events/:id/export", handler.ExportEvent)
	}
}

func (h *ExportHandler) ExportEvent(c *gin.Context) {
	eventID, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		NewErrorResponse(c, http.StatusBadRequest, "invalid event ID")
		return
	}

	zipPath, err := h.Usecase.ExportEventPhotos(uint(eventID))
	if err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}
	defer os.Remove(zipPath) // Clean up after sending

	// Set headers for download
	c.Header("Content-Description", "File Transfer")
	c.Header("Content-Transfer-Encoding", "binary")
	c.Header("Content-Disposition", fmt.Sprintf("attachment; filename=event_export_%d.zip", eventID))
	c.Header("Content-Type", "application/zip")
	c.File(zipPath)
}
