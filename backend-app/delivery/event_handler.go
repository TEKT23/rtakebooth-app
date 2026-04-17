package delivery

import (
	"net/http"
	"strconv"

	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
	"github.com/gin-gonic/gin"
)

type EventHandler struct {
	EventUsecase domain.EventUsecase
}

func NewEventHandler(r *gin.Engine, eu domain.EventUsecase) {
	handler := &EventHandler{
		EventUsecase: eu,
	}

	api := r.Group("/api/v1")
	{
		api.POST("/events", handler.CreateEvent)
		api.GET("/events", handler.GetAllEvents)
		api.GET("/events/:id", handler.GetEventByID)
		api.PUT("/events/:id", handler.UpdateEvent)
		api.DELETE("/events/:id", handler.DeleteEvent)
	}
}

func (h *EventHandler) CreateEvent(c *gin.Context) {
	var event domain.Event
	if err := c.ShouldBindJSON(&event); err != nil {
		NewErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	if err := h.EventUsecase.CreateEvent(&event); err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	NewSuccessResponse(c, http.StatusCreated, "Event created successfully", event)
}

func (h *EventHandler) GetAllEvents(c *gin.Context) {
	events, err := h.EventUsecase.GetAllEvents()
	if err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	NewSuccessResponse(c, http.StatusOK, "Events retrieved successfully", events)
}

func (h *EventHandler) GetEventByID(c *gin.Context) {
	idStr := c.Param("id")
	id, err := strconv.Atoi(idStr)
	if err != nil {
		NewErrorResponse(c, http.StatusBadRequest, "invalid ID format")
		return
	}

	event, err := h.EventUsecase.GetEventByID(uint(id))
	if err != nil {
		NewErrorResponse(c, http.StatusNotFound, "Event not found")
		return
	}

	NewSuccessResponse(c, http.StatusOK, "Event retrieved successfully", event)
}

func (h *EventHandler) UpdateEvent(c *gin.Context) {
	idStr := c.Param("id")
	id, err := strconv.Atoi(idStr)
	if err != nil {
		NewErrorResponse(c, http.StatusBadRequest, "invalid ID format")
		return
	}

	var event domain.Event
	if err := c.ShouldBindJSON(&event); err != nil {
		NewErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}
	event.ID = uint(id)

	if err := h.EventUsecase.UpdateEvent(&event); err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	NewSuccessResponse(c, http.StatusOK, "Event updated successfully", event)
}

func (h *EventHandler) DeleteEvent(c *gin.Context) {
	idStr := c.Param("id")
	id, err := strconv.Atoi(idStr)
	if err != nil {
		NewErrorResponse(c, http.StatusBadRequest, "invalid ID format")
		return
	}

	if err := h.EventUsecase.DeleteEvent(uint(id)); err != nil {
		NewErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	NewSuccessResponse(c, http.StatusOK, "Event deleted successfully", nil)
}
