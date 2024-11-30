package magym.robobt.repository.accelerometer

import magym.robobt.repository.input_device.InputDeviceData
import magym.robobt.repository.input_device.accelerometer.model.ControlMotorsData

internal data class MotorSpeedMapperTestData(
    val inputDeviceData: InputDeviceData,
    val motorsData: ControlMotorsData,
)