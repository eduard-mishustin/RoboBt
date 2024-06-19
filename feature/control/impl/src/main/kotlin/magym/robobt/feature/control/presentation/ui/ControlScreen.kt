package magym.robobt.feature.control.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import magym.robobt.common.ui.theme.RoboTheme
import magym.robobt.common.ui.theme.ScreenPreview
import magym.robobt.feature.control.presentation.ui.state.ControlUiState

@Composable
internal fun ControlScreen(
    state: ControlUiState,
) {
    Text(
        modifier = Modifier.padding(16.dp),
        fontSize = 24.sp,
        text = state.leftMotor.toString() + " : " + state.rightMotor.toString()
    )
}

@ScreenPreview
@Composable
private fun ControlScreenPreview() = RoboTheme {
    ControlScreen(ControlUiState(leftMotor = 0, rightMotor = 0))
}
