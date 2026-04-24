package org.rtakebooth.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.rtakebooth.app.navigation.NavigationState
import org.rtakebooth.app.navigation.Screen
import org.rtakebooth.app.theme.RtakeBoothTheme
import org.rtakebooth.app.ui.components.Sidebar
import org.rtakebooth.app.ui.components.StatusBar
import org.rtakebooth.app.ui.components.TopBar
import org.rtakebooth.app.ui.setup.GeneralScreen
import org.rtakebooth.app.ui.setup.CaptureScreen
import org.rtakebooth.app.ui.setup.CameraScreen
import org.rtakebooth.app.ui.setup.AttendantScreen
import org.rtakebooth.app.ui.setup.LayoutScreen
import org.rtakebooth.app.ui.setup.SharingScreen
import org.rtakebooth.app.ui.setup.PrintScreen
import org.rtakebooth.app.ui.setup.PlaceholderScreen
import org.rtakebooth.app.ui.editor.EditorScreen
import org.rtakebooth.app.ui.event.SharingStatusScreen
import org.rtakebooth.app.ui.event.ExportScreen
import org.rtakebooth.app.ui.event.EventListScreen
import org.rtakebooth.app.ui.components.AppSnackbar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun App() {
    val navigationState = remember { NavigationState() }

    RtakeBoothTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Bar
                TopBar(currentScreen = navigationState.currentScreen)

                // Main content area: Sidebar + Content
                Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    // Sidebar
                    Sidebar(
                        currentScreen = navigationState.currentScreen,
                        onNavigate = { navigationState.navigateTo(it) }
                    )

                    // ViewModels (Hoisted to preserve state across navigation)
                    val editorViewModel = remember { org.rtakebooth.app.viewmodel.EditorViewModel() }
                    val generalViewModel = remember { org.rtakebooth.app.viewmodel.GeneralViewModel() }
                    val captureViewModel = remember { org.rtakebooth.app.viewmodel.CaptureViewModel() }
                    val cameraViewModel = remember { org.rtakebooth.app.viewmodel.CameraViewModel() }
                    val attendantViewModel = remember { org.rtakebooth.app.viewmodel.AttendantViewModel() }
                    val layoutViewModel = remember { org.rtakebooth.app.viewmodel.LayoutViewModel() }
                    val sharingViewModel = remember { org.rtakebooth.app.viewmodel.SharingViewModel() }
                    val printViewModel = remember { org.rtakebooth.app.viewmodel.PrintViewModel() }
                    val templateViewModel = remember { org.rtakebooth.app.viewmodel.TemplateViewModel() }

                    // Content Area — route to the correct screen
                    Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                        when (navigationState.currentScreen) {
                            Screen.EDITOR -> EditorScreen(viewModel = editorViewModel)
                            Screen.GENERAL -> GeneralScreen(viewModel = generalViewModel)
                            Screen.CAPTURE -> CaptureScreen(viewModel = captureViewModel)
                            Screen.CAMERA -> CameraScreen(viewModel = cameraViewModel)
                            Screen.ATTENDANT -> AttendantScreen(viewModel = attendantViewModel)
                            Screen.LAYOUT -> LayoutScreen(viewModel = layoutViewModel)
                            Screen.SHARING -> SharingScreen(viewModel = sharingViewModel)
                            Screen.PRINT -> PrintScreen(viewModel = printViewModel)
                            Screen.PRESETS_TEMPLATE -> org.rtakebooth.app.ui.template.TemplateEditorScreen(viewModel = templateViewModel)
                            Screen.SHARING_STATUS -> SharingStatusScreen()
                            Screen.EXPORT -> ExportScreen()
                            Screen.EVENTS -> EventListScreen()
                            else -> PlaceholderScreen(screenName = navigationState.currentScreen.label)
                        }
                    }
                }

                // Status Bar
                StatusBar()
            }

            // Global Snackbar Overlay
            var snackbarMessage by remember { mutableStateOf<String?>(null) }
            var isSnackbarError by remember { mutableStateOf(false) }

            AppSnackbar(
                message = snackbarMessage,
                isError = isSnackbarError,
                onDismiss = { snackbarMessage = null }
            )
        }
    }
}