package magym.robobt.feature.control.presentation.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import magym.robobt.common.ui.theme.RoboTheme
import magym.robobt.common.ui.theme.ScreenPreview
import magym.robobt.feature.control.presentation.ui.state.ControlUiState

@Composable
internal fun ControlScreen(
    state: ControlUiState,
) {
    Text(text = "todo")
}

@ScreenPreview
@Composable
private fun ControlScreenPreview() = RoboTheme {
    ControlScreen(ControlUiState())
}
