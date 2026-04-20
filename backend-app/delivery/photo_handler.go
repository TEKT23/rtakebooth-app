package delivery

import (
	"net/http"
	"strconv"

	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
	"github.com/gin-gonic/gin"
)

type PhotoHandler struct {
	PhotoUsecase domain.PhotoUsecase
}

func NewPhotoHandler(r *gin.Engine, pu domain.PhotoUsecase) {
	handler := &PhotoHandler{
		PhotoUsecase: pu,
	}

	api := r.Group("/api/v1")
	{
		api.GET("/sessions/:id/photos", handler.GetPhotosBySession)
		api.GET("/events/:id/photos", handler.GetPhotosByEvent)
		api.DELETE("/photos/:id", handler.DeletePhoto)
	}
}

func (h *PhotoHandler) GetPhotosBySession(c *gin.Context) {
	id, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		NewErrorResponse(c, http.StatusBadRequest, "invalid session ID")
		return
	}

	photos, err := h.PhotoUsecase.GetPhotosBySession(uint(id))
	if err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	NewSuccessResponse(c, http.StatusOK, "Photos retrieved successfully", photos)
}

func (h *PhotoHandler) GetPhotosByEvent(c *gin.Context) {
	id, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		NewErrorResponse(c, http.StatusBadRequest, "invalid event ID")
		return
	}

	photos, err := h.PhotoUsecase.GetPhotosByEvent(uint(id))
	if err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	NewSuccessResponse(c, http.StatusOK, "Photos retrieved successfully", photos)
}

func (h *PhotoHandler) DeletePhoto(c *gin.Context) {
	id, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		NewErrorResponse(c, http.StatusBadRequest, "invalid photo ID")
		return
	}

	err = h.PhotoUsecase.DeletePhoto(c.Request.Context(), uint(id))
	if err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	NewSuccessResponse(c, http.StatusOK, "Photo deleted successfully", nil)
}
