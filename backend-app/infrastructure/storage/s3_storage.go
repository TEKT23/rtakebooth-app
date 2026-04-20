package storage

import (
	"context"
	"fmt"
	"io"
	"net/url"
	"os"
	"strings"

	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
	"github.com/aws/aws-sdk-go-v2/aws"
	"github.com/aws/aws-sdk-go-v2/config"
	"github.com/aws/aws-sdk-go-v2/credentials"
	"github.com/aws/aws-sdk-go-v2/feature/s3/manager"
	"github.com/aws/aws-sdk-go-v2/service/s3"
)

type s3StorageService struct {
	client *s3.Client
	bucket string
	region string
}

func NewS3StorageService() domain.StorageService {
	// Load config from environment
	cfg, err := config.LoadDefaultConfig(context.TODO(),
		config.WithRegion(os.Getenv("AWS_REGION")),
		config.WithCredentialsProvider(credentials.NewStaticCredentialsProvider(
			os.Getenv("AWS_ACCESS_KEY"),
			os.Getenv("AWS_SECRET_KEY"),
			"",
		)),
	)
	if err != nil {
		fmt.Printf("Error loading AWS config: %v\n", err)
	}

	return &s3StorageService{
		client: s3.NewFromConfig(cfg),
		bucket: os.Getenv("AWS_BUCKET"),
		region: os.Getenv("AWS_REGION"),
	}
}

func (s *s3StorageService) UploadFile(ctx context.Context, file io.Reader, fileName string, contentType string) (string, error) {
	uploader := manager.NewUploader(s.client)

	result, err := uploader.Upload(ctx, &s3.PutObjectInput{
		Bucket:      aws.String(s.bucket),
		Key:         aws.String(fileName),
		Body:        file,
		ContentType: aws.String(contentType),
	})

	if err != nil {
		return "", fmt.Errorf("failed to upload file to S3: %w", err)
	}

	return result.Location, nil
}

func (s *s3StorageService) DeleteFile(ctx context.Context, fileName string) error {
	_, err := s.client.DeleteObject(ctx, &s3.DeleteObjectInput{
		Bucket: aws.String(s.bucket),
		Key:    aws.String(fileName),
	})
	if err != nil {
		return fmt.Errorf("failed to delete file from S3: %w", err)
	}
	return nil
}

// ExtractS3Key extracts the S3 object key from a full S3 URL.
func ExtractS3Key(s3URL string) (string, error) {
	parsed, err := url.Parse(s3URL)
	if err != nil {
		return "", err
	}
	// Remove leading slash from path
	key := strings.TrimPrefix(parsed.Path, "/")
	return key, nil
}

