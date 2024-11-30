package magym.robobt.repository.input_device.joystick

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import magym.robobt.repository.input_device.InputDeviceData
import magym.robobt.repository.input_device.InputDeviceRepository

interface JoystickRepository : InputDeviceRepository

interface MutableJoystickRepository : JoystickRepository {

    fun onStickInputChanged(x: Float, y: Float)
}

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