package magym.robobt.feature.control.presentation.tea.model

internal data class ControlState(
    val motorsData: ControlMotorsData = ControlMotorsData(leftMotor = 0, rightMotor = 0)
)