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
		api.POST("/sessions/:id/photos", handler.UploadPhoto)
		api.GET("/sessions/:id/photos", handler.GetPhotosBySession)
		api.DELETE("/photos/:id", handler.DeletePhoto)
		api.GET("/photos/:id/download", handler.GetPresignedURL)
	}
}

func (h *PhotoHandler) UploadPhoto(c *gin.Context) {
	sessionID, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		NewErrorResponse(c, http.StatusBadRequest, "invalid session ID")
		return
	}

	file, _, err := c.Request.FormFile("file")
	if err != nil {
		NewErrorResponse(c, http.StatusBadRequest, "file is required")
		return
	}
	defer file.Close()

	fileType := c.PostForm("type")
	if fileType == "" {
		fileType = "raw"
	}

	photo, err := h.PhotoUsecase.UploadPhoto(c.Request.Context(), uint(sessionID), file, fileType)
	if err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	NewSuccessResponse(c, http.StatusCreated, "Photo uploaded successfully", photo)
}

func (h *PhotoHandler) GetPhotosBySession(c *gin.Context) {
	sessionID, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		NewErrorResponse(c, http.StatusBadRequest, "invalid session ID")
		return
	}

	photos, err := h.PhotoUsecase.GetPhotosBySession(uint(sessionID))
	if err != nil {
		NewErrorResponse(c, http.StatusNotFound, err.Error())
		return
	}

	NewSuccessResponse(c, http.StatusOK, "Photos retrieved successfully", photos)
}

func (h *PhotoHandler) DeletePhoto(c *gin.Context) {
	photoID, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		NewErrorResponse(c, http.StatusBadRequest, "invalid photo ID")
		return
	}

	err = h.PhotoUsecase.DeletePhoto(c.Request.Context(), uint(photoID))
	if err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	NewSuccessResponse(c, http.StatusOK, "Photo deleted successfully", nil)
}

func (h *PhotoHandler) GetPresignedURL(c *gin.Context) {
	photoID, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		NewErrorResponse(c, http.StatusBadRequest, "invalid photo ID")
		return
	}

	url, err := h.PhotoUsecase.GetPresignedURL(c.Request.Context(), uint(photoID))
	if err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	NewSuccessResponse(c, http.StatusOK, "Presigned URL generated", gin.H{
		"url": url,
	})
}
