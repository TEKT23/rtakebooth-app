package org.rtakebooth.app.ui.setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.rtakebooth.app.ui.components.*
import org.rtakebooth.app.viewmodel.SharingViewModel

@Composable
fun SharingScreen(viewModel: SharingViewModel = remember { SharingViewModel() }) {
    val settings = viewModel.settings
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Scrollable form content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(bottom = 16.dp)
            ) {
                // Loading indicator
                if (viewModel.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                // === SECTION: fotoShare Cloud ===
                SectionHeader("fotoShare Cloud")

                ToggleRow(
                    label = "Enhanced Sharing",
                    checked = settings.enhancedSharingEnabled,
                    onCheckedChange = { viewModel.updateSettings(settings.copy(enhancedSharingEnabled = it)) },
                    description = "Enable cloud-based photo sharing"
                )
                ToggleRow(
                    label = "Upload Original Photos",
                    checked = settings.uploadOriginalPhotos,
                    onCheckedChange = { viewModel.updateSettings(settings.copy(uploadOriginalPhotos = it)) }
                )

                // === SECTION: Email ===
                SectionHeader("Email")

                ToggleRow(
                    label = "Enable Email",
                    checked = settings.emailEnabled,
                    onCheckedChange = { viewModel.updateSettings(settings.copy(emailEnabled = it)) }
                )
                if (settings.emailEnabled) {
                    ToggleRow(
                        label = "Mask E-mail",
                        checked = settings.emailMasked,
                        onCheckedChange = { viewModel.updateSettings(settings.copy(emailMasked = it)) }
                    )
                    TextFieldRow(
                        label = "Subject",
                        value = settings.emailSubject,
                        onValueChange = { viewModel.updateSettings(settings.copy(emailSubject = it)) }
                    )
                    LargeTextAreaRow(
                        label = "HTML Body",
                        value = settings.emailHtmlBody,
                        onValueChange = { viewModel.updateSettings(settings.copy(emailHtmlBody = it)) },
                        placeholder = "Use [image_url] and [share_icon] as placeholders"
                    )
                }

                // === SECTION: WhatsApp & SMS ===
                SectionHeader("WhatsApp & SMS")

                ToggleRow(
                    label = "Enable WhatsApp",
                    checked = settings.whatsappEnabled,
                    onCheckedChange = { viewModel.updateSettings(settings.copy(whatsappEnabled = it)) }
                )
                if (settings.whatsappEnabled) {
                    LargeTextAreaRow(
                        label = "WhatsApp Message Template",
                        value = settings.whatsappMessage,
                        onValueChange = { viewModel.updateSettings(settings.copy(whatsappMessage = it)) },
                        minLines = 3
                    )
                    TextFieldRow(
                        label = "WhatsApp Country Code",
                        value = settings.whatsappCountryCode,
                        onValueChange = { viewModel.updateSettings(settings.copy(whatsappCountryCode = it)) }
                    )
                }

                ToggleRow(
                    label = "Enable SMS",
                    checked = settings.smsEnabled,
                    onCheckedChange = { viewModel.updateSettings(settings.copy(smsEnabled = it)) }
                )
                if (settings.smsEnabled) {
                    LargeTextAreaRow(
                        label = "SMS Message Template",
                        value = settings.smsMessage,
                        onValueChange = { viewModel.updateSettings(settings.copy(smsMessage = it)) },
                        minLines = 3
                    )
                    TextFieldRow(
                        label = "SMS Country Code",
                        value = settings.smsCountryCode,
                        onValueChange = { viewModel.updateSettings(settings.copy(smsCountryCode = it)) }
                    )
                }

                DropdownRow(
                    label = "Messaging Provider",
                    selectedValue = settings.messagingProvider.ifEmpty { "Default" },
                    options = listOf("Default", "Twilio", "Custom API"),
                    onValueChange = { viewModel.updateSettings(settings.copy(messagingProvider = it)) }
                )

                // === SECTION: Sharing Screen UI ===
                SectionHeader("Sharing Screen UI")

                ToggleRow(
                    label = "Skip Sharing Screen",
                    checked = settings.skipSharingScreen,
                    onCheckedChange = { viewModel.updateSettings(settings.copy(skipSharingScreen = it)) },
                    description = "Skip the sharing screen entirely after capture"
                )
                ToggleRow(
                    label = "Show Done Button",
                    checked = settings.showDoneButton,
                    onCheckedChange = { viewModel.updateSettings(settings.copy(showDoneButton = it)) }
                )
                ToggleRow(
                    label = "Show Retake Button",
                    checked = settings.showRetakeButton,
                    onCheckedChange = { viewModel.updateSettings(settings.copy(showRetakeButton = it)) }
                )
                SliderRow(
                    label = "Final Screen Timeout",
                    value = settings.finalScreenTimeout.toFloat(),
                    onValueChange = { viewModel.updateSettings(settings.copy(finalScreenTimeout = it.toInt())) },
                    valueRange = 5f..120f,
                    unit = "s"
                )
            }

            // Bottom bar: Messages + Save button
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status messages
                Column(modifier = Modifier.weight(1f)) {
                    viewModel.saveMessage?.let {
                        Text(
                            text = it,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    viewModel.errorMessage?.let {
                        Text(
                            text = it,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                // Save button
                Button(
                    onClick = { viewModel.saveSettings() },
                    enabled = !viewModel.isSaving
                ) {
                    if (viewModel.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(if (viewModel.isSaving) "Saving..." else "Save Settings")
                }
            }
        }
    }
}
