package magym.robobt.feature.control.presentation.tea.model

import magym.robobt.common.pure.model.ControlMotorsData

internal data class ControlState(
    val motorsData: ControlMotorsData = ControlMotorsData(leftMotor = 0, rightMotor = 0),
    val controlMode: ControlMode = ControlMode.Manual,
    val temperature: Double = 0.0,
    val humidity: Double = 0.0,
)