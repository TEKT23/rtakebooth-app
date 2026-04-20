package org.rtakebooth.app.data.model

enum class ScreenTab { WELCOME, CAPTURE, SHARING }

enum class AspectRatio(val label: String, val widthRatio: Int, val heightRatio: Int) {
    RATIO_16_9("16:9", 16, 9),
    RATIO_4_3("4:3", 4, 3),
    RATIO_1_1("1:1", 1, 1),
    RATIO_9_16("9:16", 9, 16),
}

enum class Orientation { LANDSCAPE, PORTRAIT }

enum class SessionTrigger(val label: String) {
    TOUCH_SCREEN("Touch Screen"),
    SPACE_BAR("Space Bar"),
    FUNCTION_KEY("Function Key"),
}

data class EditorState(
    val currentTab: ScreenTab = ScreenTab.WELCOME,
    val canvasElements: Map<ScreenTab, List<CanvasElement>> = mapOf(
        ScreenTab.WELCOME to emptyList(),
        ScreenTab.CAPTURE to emptyList(),
        ScreenTab.SHARING to emptyList(),
    ),
    val selectedElementId: String? = null,
    val aspectRatio: AspectRatio = AspectRatio.RATIO_16_9,
    val orientation: Orientation = Orientation.LANDSCAPE,
    val showLiveView: Boolean = true,
    val cropLiveView: Boolean = false,
    val mirrorLiveView: Boolean = false,
    val showGalleryButton: Boolean = true,
    val showCountdown: Boolean = true,
    val showCancelButton: Boolean = true,
    val sessionTrigger: SessionTrigger = SessionTrigger.TOUCH_SCREEN,
    val canvasBackgroundColor: String = "#1a1a2e",
)
