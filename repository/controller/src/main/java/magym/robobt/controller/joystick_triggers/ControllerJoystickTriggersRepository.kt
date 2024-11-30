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
        val leftMotor =
            if (flow.value.leftMotor < 0) flow.value.leftMotor
            else (rightTrigger * 255).toInt()

        val rightMotor =
            if (flow.value.rightMotor < 0) flow.value.rightMotor
            else (leftTrigger * 255).toInt()

        flow.tryEmit(ControlMotorsData(leftMotor, rightMotor))
    }

    override fun onLeftButtonDown() {
        val value = ControlMotorsData(
            leftMotor = flow.value.leftMotor,
            rightMotor = -255
        )

        flow.tryEmit(value)
    }

    override fun onRightButtonDown() {
        val value = ControlMotorsData(
            leftMotor = -255,
            rightMotor = flow.value.rightMotor
        )

        flow.tryEmit(value)
    }

    override fun onLeftButtonUp() {
        val value = ControlMotorsData(
            leftMotor = flow.value.leftMotor,
            rightMotor = 0
        )

        flow.tryEmit(value)
    }

    override fun onRightButtonUp() {
        val value = ControlMotorsData(
            leftMotor = 0,
            rightMotor = flow.value.rightMotor
        )

        flow.tryEmit(value)
    }
}