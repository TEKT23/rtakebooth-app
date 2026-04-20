package org.rtakebooth.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

private val ROW_PADDING = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

/**
 * Section header — bold title for grouping form fields.
 */
@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 4.dp)
    )
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
    )
}

/**
 * Toggle row — label on left, Switch on right.
 * Optional [description] shows a small subtitle under the label.
 */
@Composable
fun ToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    description: String? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(ROW_PADDING),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (description != null) {
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

/**
 * Text field row — label above, OutlinedTextField below.
 * Set [isPassword] = true for password fields.
 */
@Composable
fun TextFieldRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    isPassword: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(ROW_PADDING)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, fontSize = 13.sp) },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            singleLine = true,
        )
    }
}

/**
 * Slider row — label + current value display above, Slider below.
 */
@Composable
fun SliderRow(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int = 0,
    unit: String = "",
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(ROW_PADDING)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "%.1f%s".format(value, if (unit.isNotEmpty()) " $unit" else ""),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/**
 * Dropdown row — label above, clickable field that opens a dropdown menu.
 */
@Composable
fun DropdownRow(
    label: String,
    selectedValue: String,
    options: List<String>,
    onValueChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(ROW_PADDING)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box {
            OutlinedTextField(
                value = selectedValue,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Text(if (expanded) "▲" else "▼", fontSize = 12.sp)
                    }
                },
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

/**
 * File picker row — label above, read-only path field + Browse button.
 * Uses AWT FileDialog for native file picking.
 */
@Composable
fun FilePickerRow(
    label: String,
    path: String,
    onPathSelected: (String) -> Unit,
    pickDirectory: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(if (label.isNotEmpty()) ROW_PADDING else PaddingValues(horizontal = 0.dp, vertical = 0.dp))
    ) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = path,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Select path...", fontSize = 13.sp) },
                singleLine = true,
            )
            Button(
                onClick = {
                    if (pickDirectory) {
                        // Use JVM system property trick for directory selection
                        System.setProperty("apple.awt.fileDialogForDirectories", "true")
                        val dialog = FileDialog(null as Frame?, "Select Directory", FileDialog.LOAD)
                        dialog.isVisible = true
                        if (dialog.directory != null) {
                            onPathSelected(dialog.directory + (dialog.file ?: ""))
                        }
                        System.setProperty("apple.awt.fileDialogForDirectories", "false")
                    } else {
                        val dialog = FileDialog(null as Frame?, "Select File", FileDialog.LOAD)
                        dialog.isVisible = true
                        if (dialog.file != null) {
                            onPathSelected(dialog.directory + dialog.file)
                        }
                    }
                }
            ) {
                Text("Browse")
            }
        }
    }
}

/**
 * Radio group row — label above, radio buttons listed below.
 */
@Composable
fun RadioGroupRow(
    label: String,
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(ROW_PADDING)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    RadioButton(
                        selected = selectedOption == option,
                        onClick = { onOptionSelected(option) }
                    )
                    Text(
                        text = option,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}

/**
 * Action button row — descriptive label on left, action button on right.
 */
@Composable
fun ActionButtonRow(
    label: String,
    buttonText: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(ROW_PADDING),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f).padding(end = 8.dp)
        )
        OutlinedButton(onClick = onClick) {
            Text(buttonText)
        }
    }
}

/**
 * Checkbox row — checkbox on left, label text on right.
 */
@Composable
fun CheckboxRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(ROW_PADDING),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

/**
 * Large text area row — label above, multi-line text field below.
 */
@Composable
fun LargeTextAreaRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    minLines: Int = 5,
    placeholder: String = "",
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(ROW_PADDING)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            minLines = minLines,
            placeholder = { Text(placeholder, fontSize = 13.sp) },
        )
    }
}
