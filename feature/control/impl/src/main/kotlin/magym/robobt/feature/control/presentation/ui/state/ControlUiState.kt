package magym.robobt.feature.control.presentation.ui.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class ControlUiState(
    val leftMotor: Int,
    val rightMotor: Int,
)