package magym.robobt.common.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
annotation class WidgetPreview

@Preview(
    device = "id:pixel_8_pro",
    showBackground = true
)
annotation class ScreenPreview

@Preview(
    device = "spec:parent=pixel_8_pro,orientation=landscape",
    showBackground = true
)
annotation class LandscapeScreenPreview

@Composable
fun PreviewTheme(
    content: @Composable () -> Unit,
) {
    RoboTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}