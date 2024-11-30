package magym.robobt.controller.joystick_triggers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import magym.robobt.controller.ControlMotorsData
import magym.robobt.controller.ControllerRepository

interface ControllerJoystickTriggersRepository : ControllerRepository

interface MutableControllerJoystickTriggersRepository : ControllerJoystickTriggersRepository {

    fun onTriggerInputChanged(leftTrigger: Float, rightTrigger: Float)

    fun onLeftButtonDown()

    fun onRightButtonDown()

    fun onLeftButtonUp()

    fun onRightButtonUp()
}

internal class ControllerJoystickTriggersRepositoryImpl : ControllerJoystickTriggersRepository, MutableControllerJoystickTriggersRepository {

    private val flow = MutableStateFlow(ControlMotorsData.empty())

    override fun connect(): Flow<ControlMotorsData> {
        return flow
    }

    override fun onTriggerInputChanged(leftTrigger: Float, rightTrigger: Float) {
        val data = flow.value

        val leftMotor =
            if (data.leftMotor < 0) data.leftMotor
            else (rightTrigger * 255).toInt()

        val rightMotor =
            if (data.rightMotor < 0) data.rightMotor
            else (leftTrigger * 255).toInt()

        flow.tryEmit(ControlMotorsData(leftMotor, rightMotor))
    }

    override fun onLeftButtonDown() {
        val data = flow.value

        val value = ControlMotorsData(
            leftMotor = data.leftMotor,
            rightMotor = -255
        )

        flow.tryEmit(value)
    }

    override fun onRightButtonDown() {
        val data = flow.value

        val value = ControlMotorsData(
            leftMotor = -255,
            rightMotor = data.rightMotor
        )

        flow.tryEmit(value)
    }

    override fun onLeftButtonUp() {
        val data = flow.value

        val value = ControlMotorsData(
            leftMotor = data.leftMotor,
            rightMotor = 0
        )

        flow.tryEmit(value)
    }

    override fun onRightButtonUp() {
        val data = flow.value

        val value = ControlMotorsData(
            leftMotor = 0,
            rightMotor = data.rightMotor
        )

        flow.tryEmit(value)
    }
}