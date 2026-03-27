package delivery

import (
	"net/http"

	"github.com/TEKT23/rtakebooth-app/backend-app/usecase"
	"github.com/gin-gonic/gin"
)

type SessionHandler struct {
	SessionUsecase usecase.SessionUsecase
}

func NewSessionHandler(r *gin.Engine, su usecase.SessionUsecase) {
	handler := &SessionHandler{
		SessionUsecase: su,
	}

	r.POST("/sessions", handler.CreateSession)
	r.GET("/sessions/:id/status", handler.GetSessionStatus)
	r.POST("/internal/webhook/sessions/:id/paid", handler.SetPaymentPaid)
	r.POST("/sessions/:id/photos", handler.UploadSessionPhoto)
}

func (h *SessionHandler) CreateSession(c *gin.Context) {
	session, err := h.SessionUsecase.CreateSession()
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusCreated, session)
}

func (h *SessionHandler) GetSessionStatus(c *gin.Context) {
	var id struct {
		ID uint `uri:"id" binding:"required"`
	}
	if err := c.ShouldBindUri(&id); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	status, err := h.SessionUsecase.GetSessionStatus(id.ID)
	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "Session not found"})
		return
	}

	c.JSON(http.StatusOK, gin.H{
		"session_id": id.ID,
		"status":     status,
	})
}

func (h *SessionHandler) SetPaymentPaid(c *gin.Context) {
	var id struct {
		ID uint `uri:"id" binding:"required"`
	}
	if err := c.ShouldBindUri(&id); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	err := h.SessionUsecase.SetPaymentPaid(id.ID)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{"message": "Payment status updated to PAID"})
}

func (h *SessionHandler) UploadSessionPhoto(c *gin.Context) {
	// 1. Ambil ID Sesi dari URI
	var uri struct {
		ID uint `uri:"id" binding:"required"`
	}
	if err := c.ShouldBindUri(&uri); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	// 2. Ambil File dari Multipart Form
	file, _, err := c.Request.FormFile("file")
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "file is required"})
		return
	}
	defer file.Close()

	// 3. Ambil Tipe dari Form
	fileType := c.PostForm("type")
	if fileType == "" {
		fileType = "raw"
	}

	// 4. Jalankan Usecase
	photo, err := h.SessionUsecase.UploadSessionPhoto(c.Request.Context(), uri.ID, file, fileType)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusCreated, photo)
}
