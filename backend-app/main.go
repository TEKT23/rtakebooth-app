package main

import (
	"log"
	"os"

	"github.com/TEKT23/rtakebooth-app/backend-app/config"
	"github.com/TEKT23/rtakebooth-app/backend-app/delivery"
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

	// Start Server
	port := os.Getenv("SERVER_PORT")
	if port == "" {
		port = "8080"
	}

	log.Printf("Server starting on port %s", port)
	r.Run(":" + port)
}
