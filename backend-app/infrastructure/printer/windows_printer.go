package printer

import (
	"fmt"
	"os/exec"

	"github.com/TEKT23/rtakebooth-app/backend-app/domain"
)

type windowsPrinterService struct{}

func NewWindowsPrinterService() domain.PrinterService {
	return &windowsPrinterService{}
}

func (s *windowsPrinterService) PrintFile(filePath string, printerName string, copies int) error {
	// PowerShell command to print an image.
	// We use Start-Process with the -Verb Print to use the default Windows photo printing.
	// If printerName is provided, we might need a more complex solution or set it as default temporarily.
	
	// For simplicity in booth environment, we assume the DNP printer is already set as Default.
	// If printerName is specified, we try to set it as default first or use a specific command.
	
	var cmdStr string
	if printerName != "" {
		// Set default printer temporarily and print
		cmdStr = fmt.Sprintf("Set-UintPrinter -Name '%s'; Start-Process -FilePath '%s' -Verb Print", printerName, filePath)
	} else {
		cmdStr = fmt.Sprintf("Start-Process -FilePath '%s' -Verb Print", filePath)
	}

	cmd := exec.Command("powershell", "-Command", cmdStr)
	output, err := cmd.CombinedOutput()
	if err != nil {
		return fmt.Errorf("powershell print failed: %w, output: %s", err, string(output))
	}

	return nil
}
