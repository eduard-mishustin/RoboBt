package magym.robobt.controller.mapper

import magym.robobt.controller.ControlMotorsData
import magym.robobt.repository.input_device.InputDeviceData

internal data class MotorSpeedMapperTestData(
    val inputDeviceData: InputDeviceData,
    val motorsData: ControlMotorsData,
)