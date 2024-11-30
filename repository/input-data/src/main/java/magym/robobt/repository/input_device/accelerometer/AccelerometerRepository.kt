package magym.robobt.repository.input_device.accelerometer

import kotlinx.coroutines.flow.MutableStateFlow
import magym.robobt.repository.input_device.InputDeviceRepository
import magym.robobt.repository.input_device.accelerometer.model.ControlMotorsData

interface AccelerometerRepository : InputDeviceRepository

val joystickTriggersFlow = MutableStateFlow(ControlMotorsData.empty())
