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
		api.GET("/events/:id/photos", handler.GetPhotosByEvent)
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

	// 2. Ambil File dari Multipart Form
	file, fileHeader, err := c.Request.FormFile("file")
	if err != nil {
		NewErrorResponse(c, http.StatusBadRequest, "file is required")
		return
	}
	defer file.Close()

	// 3. Deteksi MIME Type secara dinamis
	mimeType := fileHeader.Header.Get("Content-Type")
	if mimeType == "" {
		mimeType = "image/jpeg" // Default fallback
	}

	// 4. Ambil Tipe Kategori dari Form (raw_final, result_live, dll)
	fileType := c.PostForm("type")
	if fileType == "" {
		fileType = "raw"
	}

	// 5. Jalankan Usecase
	photo, err := h.PhotoUsecase.UploadPhoto(c.Request.Context(), uint(sessionID), file, mimeType, fileType)
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

func (h *PhotoHandler) GetPhotosByEvent(c *gin.Context) {
	eventID, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		NewErrorResponse(c, http.StatusBadRequest, "invalid event ID")
		return
	}

	photos, err := h.PhotoUsecase.GetPhotosByEvent(uint(eventID))
	if err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
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
