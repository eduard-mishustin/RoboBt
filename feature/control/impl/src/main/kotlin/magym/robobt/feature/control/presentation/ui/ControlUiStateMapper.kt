package magym.robobt.feature.control.presentation.ui

import magym.robobt.common.tea.component.UiStateMapper
import magym.robobt.feature.control.presentation.tea.model.ControlMode
import magym.robobt.feature.control.presentation.tea.model.ControlState
import magym.robobt.feature.control.presentation.ui.state.ControlUiState

internal class ControlUiStateMapper : UiStateMapper<ControlState, ControlUiState> {

    override fun map(state: ControlState): ControlUiState {
        return when (state.controlMode) {
            ControlMode.Manual -> ControlUiState.Manual(
                leftMotor = state.motorsData.leftMotor,
                rightMotor = state.motorsData.rightMotor,
                weather = "${state.temperature}°C, ${state.humidity}%",
            )

            ControlMode.Accelerometer -> ControlUiState.Accelerometer(
                leftMotor = state.motorsData.leftMotor,
                rightMotor = state.motorsData.rightMotor,
                weather = "${state.temperature}°C, ${state.humidity}%",
            )
        }
    }
}