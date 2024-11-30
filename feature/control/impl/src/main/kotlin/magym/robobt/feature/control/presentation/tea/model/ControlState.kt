package magym.robobt.feature.control.presentation.tea.model

import magym.robobt.controller.ControlMotorsData

internal data class ControlState(
    val motorsData: ControlMotorsData = ControlMotorsData(leftMotor = 0, rightMotor = 0),
    val controlMode: ControlMode = ControlMode.Accelerometer,
    val temperature: Double = 0.0,
    val humidity: Double = 0.0,
)