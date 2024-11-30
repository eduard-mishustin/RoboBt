package magym.robobt.repository.input_device.joystick.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import magym.robobt.repository.input_device.InputDeviceData
import magym.robobt.repository.input_device.joystick.JoystickRepository

val joystickFlow = MutableStateFlow(InputDeviceData(0f, 0f))

internal class JoystickRepositoryImpl : JoystickRepository {

    override fun connect(): Flow<InputDeviceData> {
        return joystickFlow
    }
}