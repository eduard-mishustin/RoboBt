package magym.robobt.repository.accelerometer

import magym.robobt.repository.accelerometer.model.AccelerometerData
import magym.robobt.repository.accelerometer.model.ControlMotorsData

internal data class MotorSpeedMapperTestData(
    val accelerometerData: AccelerometerData,
    val motorsData: ControlMotorsData,
)