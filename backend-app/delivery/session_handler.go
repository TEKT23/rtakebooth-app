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
}

func (h *SessionHandler) CreateSession(c *gin.Context) {
	session, err := h.SessionUsecase.CreateSession()
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusCreated, session)
}
