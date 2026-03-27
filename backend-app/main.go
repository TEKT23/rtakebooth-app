package main

import (
	"log"
	"os"

	"github.com/TEKT23/rtakebooth-app/backend-app/config"
	"github.com/TEKT23/rtakebooth-app/backend-app/delivery"
	"github.com/TEKT23/rtakebooth-app/backend-app/infrastructure/payment"
	"github.com/TEKT23/rtakebooth-app/backend-app/infrastructure/storage"
	"github.com/TEKT23/rtakebooth-app/backend-app/repository"
	"github.com/TEKT23/rtakebooth-app/backend-app/usecase"
	"github.com/gin-gonic/gin"
	"github.com/joho/godotenv"
)

func main() {
	// Load environment variables
	godotenv.Load()

	// Initialize Database
	config.InitDB()

	// Setup Gin
	r := gin.Default()

	// Initialize Layers
	healthUsecase := usecase.NewHealthUsecase()
	delivery.NewHealthHandler(r, healthUsecase)

	sessionRepo := repository.NewSessionRepository(config.DB)
	photoRepo := repository.NewPhotoRepository(config.DB)
	mockPayment := payment.NewMockPaymentProvider()
	s3Storage := storage.NewS3StorageService()

	sessionUsecase := usecase.NewSessionUsecase(sessionRepo, photoRepo, mockPayment, s3Storage)
	delivery.NewSessionHandler(r, sessionUsecase)

	// Start Server
	port := os.Getenv("SERVER_PORT")
	if port == "" {
		port = "8080"
	}

	log.Printf("Server starting on port %s", port)
	r.Run(":" + port)
}
