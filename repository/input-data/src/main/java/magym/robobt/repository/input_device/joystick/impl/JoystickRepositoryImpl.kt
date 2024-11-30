package magym.robobt.repository.input_device.joystick.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import magym.robobt.repository.input_device.InputDeviceData
import magym.robobt.repository.input_device.joystick.JoystickRepository
import magym.robobt.repository.input_device.joystick.MutableJoystickRepository

internal class JoystickRepositoryImpl : JoystickRepository, MutableJoystickRepository {

    private val joystickFlow = MutableStateFlow(InputDeviceData.empty())

    override fun connect(): Flow<InputDeviceData> {
        return joystickFlow
    }

    override fun onStickInputChanged(x: Float, y: Float) {
        val data = InputDeviceData(
            x = x * 10,
            y = y * (-10)
        )

        joystickFlow.tryEmit(data)
    }
}