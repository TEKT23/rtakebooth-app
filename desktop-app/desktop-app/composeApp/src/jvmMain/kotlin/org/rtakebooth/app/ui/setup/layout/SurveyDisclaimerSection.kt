package org.rtakebooth.app.ui.setup.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.rtakebooth.app.ui.components.*
import org.rtakebooth.app.viewmodel.LayoutViewModel

@Composable
fun SurveyDisclaimerSection(viewModel: LayoutViewModel) {
    val settings = viewModel.settings
    var newQuestionText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        // === SURVEY SECTION ===
        SectionHeader("Survey Settings")
        ToggleRow(
            label = "Enable Guest Survey",
            checked = settings.surveyEnabled,
            onCheckedChange = { viewModel.updateSurveyEnabled(it) }
        )

        if (settings.surveyEnabled) {
            SliderRow(
                label = "Survey Font Size",
                value = settings.surveyFontSize,
                onValueChange = { viewModel.updateSurveyFontSize(it) },
                valueRange = 8f..32f,
                unit = "pt"
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Survey Questions", style = MaterialTheme.typography.titleSmall)
            
            // Add Question Input
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newQuestionText,
                    onValueChange = { newQuestionText = it },
                    label = { Text("New Question") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (newQuestionText.isNotBlank()) {
                            viewModel.addSurveyQuestion(newQuestionText)
                            newQuestionText = ""
                        }
                    }
                ) {
                    Text("+", fontSize = 24.sp)
                }
            }

            // Question List
            settings.surveyQuestions.forEach { question ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(question.question, modifier = Modifier.weight(1f))
                    IconButton(onClick = { viewModel.removeSurveyQuestion(question.id) }) {
                        Text("🗑", color = MaterialTheme.colorScheme.error, fontSize = 18.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        
        // === DISCLAIMER SECTION ===
        SectionHeader("Disclaimer & Privacy")
        ToggleRow(
            label = "Enable Terms Disclaimer",
            checked = settings.disclaimerEnabled,
            onCheckedChange = { viewModel.updateDisclaimerEnabled(it) }
        )

        if (settings.disclaimerEnabled) {
            TextFieldRow(
                label = "Header Text",
                value = settings.disclaimerHeader,
                onValueChange = { viewModel.updateDisclaimerHeader(it) }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            Text("Disclaimer Content", style = MaterialTheme.typography.labelMedium)
            OutlinedTextField(
                value = settings.disclaimerContent,
                onValueChange = { viewModel.updateDisclaimerContent(it) },
                modifier = Modifier.fillMaxWidth().height(150.dp).padding(top = 4.dp),
                placeholder = { Text("Enter your Terms and Conditions here...") }
            )
        }
    }
}
