package magym.robobt.feature.control.presentation.tea.actor

import magym.robobt.feature.control.presentation.tea.model.ControlMotorsData
import magym.robobt.repository.accelerometer.model.AccelerometerData

internal data class MotorSpeedMapperTestData(
    val accelerometerData: AccelerometerData,
    val motorsData: ControlMotorsData,
)