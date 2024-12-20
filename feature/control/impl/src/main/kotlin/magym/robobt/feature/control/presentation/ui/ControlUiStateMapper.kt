package magym.robobt.feature.control.presentation.ui

import magym.robobt.common.tea.component.UiStateMapper
import magym.robobt.feature.control.presentation.tea.model.ControlMode
import magym.robobt.feature.control.presentation.tea.model.ControlState
import magym.robobt.feature.control.presentation.ui.state.ControlUiState

internal class ControlUiStateMapper : UiStateMapper<ControlState, ControlUiState> {

    override fun map(state: ControlState): ControlUiState {
        val header = buildHeader(state)

        return when (state.controlMode) {
            ControlMode.Manual -> ControlUiState.Manual(header)
            ControlMode.Accelerometer -> ControlUiState.Accelerometer(header)
        }
    }

    private fun buildHeader(state: ControlState): ControlUiState.Header {
        return ControlUiState.Header(
            leftMotor = state.motorsData.leftMotor,
            rightMotor = state.motorsData.rightMotor,
            weather = buildWeather(state),
        )
    }

    private fun buildWeather(state: ControlState): String? {
        if (state.temperature == 0.0 && state.humidity == 0.0) {
            return null
        }

        return "${state.temperature}°C, ${state.humidity}%"
    }
}