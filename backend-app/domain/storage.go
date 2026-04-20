package domain

import (
	"context"
	"io"
)

type StorageService interface {
	UploadFile(ctx context.Context, file io.Reader, fileName string, contentType string) (string, error)
	DeleteFile(ctx context.Context, fileName string) error
}
