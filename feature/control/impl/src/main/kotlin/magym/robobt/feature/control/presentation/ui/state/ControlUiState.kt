package magym.robobt.feature.control.presentation.ui.state

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface ControlUiState {

    data class Accelerometer(
        val leftMotor: Int,
        val rightMotor: Int,
        val weather: String,
    ) : ControlUiState

    data class Manual(
        val weather: String,
    ) : ControlUiState
}