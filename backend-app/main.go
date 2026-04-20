package main

import (
	"log"
	"os"
	"time"

	"github.com/TEKT23/rtakebooth-app/backend-app/config"
	"github.com/TEKT23/rtakebooth-app/backend-app/delivery"
	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
	"github.com/TEKT23/rtakebooth-app/backend-app/infrastructure/payment"
	"github.com/TEKT23/rtakebooth-app/backend-app/infrastructure/storage"
	"github.com/TEKT23/rtakebooth-app/backend-app/repository"
	"github.com/TEKT23/rtakebooth-app/backend-app/usecase"
	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
	"github.com/joho/godotenv"
)

func main() {
	// Load environment variables (mencoba di folder saat ini dan folder backend-app jika di-run dari root)
	err := godotenv.Load()
	if err != nil {
		err = godotenv.Load("backend-app/.env")
		if err != nil {
			log.Println("Warning: No .env file found. Application will use system environment variables.")
		}
	}
	// Initialize Database
	config.InitDB()

	// Setup Gin
	r := gin.Default()

	// CORS Middleware
	r.Use(cors.New(cors.Config{
		AllowOrigins:     []string{"*"},
		AllowMethods:     []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowHeaders:     []string{"Origin", "Content-Type", "Accept", "Authorization"},
		ExposeHeaders:    []string{"Content-Length"},
		AllowCredentials: true,
		MaxAge:           12 * time.Hour,
	}))

	// Initialize Repositories
	eventRepo := repository.NewEventRepository(config.DB)
	settingRepo := repository.NewSettingRepository(config.DB)
	sessionRepo := repository.NewSessionRepository(config.DB)
	photoRepo := repository.NewPhotoRepository(config.DB)
	shareQueueRepo := repository.NewShareQueueRepository(config.DB)

	// Initialize Infrastructure
	mockPayment := payment.NewMockPaymentProvider()
	s3Storage := storage.NewS3StorageService()

	// Initialize Usecases & Handlers
	healthUsecase := usecase.NewHealthUsecase()
	delivery.NewHealthHandler(r, healthUsecase)

	eventUsecase := usecase.NewEventUsecase(eventRepo)
	delivery.NewEventHandler(r, eventUsecase)

	settingUsecase := usecase.NewSettingUsecase(settingRepo)
	delivery.NewSettingHandler(r, settingUsecase)

	sessionUsecase := usecase.NewSessionUsecase(sessionRepo, photoRepo, eventRepo, mockPayment, s3Storage)
	delivery.NewSessionHandler(r, sessionUsecase)

	photoUsecase := usecase.NewPhotoUsecase(photoRepo, sessionRepo, s3Storage)
	delivery.NewPhotoHandler(r, photoUsecase)

	shareQueueUsecase := usecase.NewShareQueueUsecase(shareQueueRepo)
	delivery.NewShareQueueHandler(r, shareQueueUsecase)

	// Seeder Dasar
	seedDefaultSettings(settingUsecase)

	// Start Server
	port := os.Getenv("SERVER_PORT")
	if port == "" {
		port = "8080"
	}

	log.Printf("Server starting on port %s", port)
	r.Run(":" + port)
}

func seedDefaultSettings(su domain.SettingUsecase) {
	categories := []string{"general", "print", "sharing", "capture", "camera", "attendant", "layout"}
	for _, cat := range categories {
		settings, _ := su.GetSettings(cat)
		if len(settings) == 0 {
			_ = su.SaveSettings(cat, make(map[string]interface{}))
		}
	}
}
