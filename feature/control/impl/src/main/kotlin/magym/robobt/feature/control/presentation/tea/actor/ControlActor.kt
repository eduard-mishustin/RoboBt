package magym.robobt.feature.control.presentation.tea.actor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import magym.robobt.common.tea.component.Actor
import magym.robobt.controller.ControlMotorsData
import magym.robobt.controller.accelerometer.ControllerAccelerometerRepository
import magym.robobt.controller.joystick.ControllerJoystickRepository
import magym.robobt.controller.joystick_triggers.ControllerJoystickTriggersRepository
import magym.robobt.feature.control.presentation.tea.core.ControlCommand
import magym.robobt.feature.control.presentation.tea.core.ControlCommand.ControlMode
import magym.robobt.feature.control.presentation.tea.core.ControlCommand.ControlMode.Accelerometer
import magym.robobt.feature.control.presentation.tea.core.ControlCommand.ControlMode.Manual
import magym.robobt.feature.control.presentation.tea.core.ControlEvent
import magym.robobt.feature.control.presentation.tea.core.ControlEvent.Controlling
import magym.robobt.repository.connect.bluetooth.BluetoothRepository
import magym.robobt.repository.connect.bluetooth.model.BluetoothOutputData

internal class ControlActor(
    private val bluetoothRepository: BluetoothRepository,
    private val controllerAccelerometerRepository: ControllerAccelerometerRepository,
    private val controllerJoystickRepository: ControllerJoystickRepository,
    private val controllerJoystickTriggersRepository: ControllerJoystickTriggersRepository,
) : Actor<ControlCommand, ControlEvent> {

    override fun act(commands: Flow<ControlCommand>): Flow<ControlEvent> {
        return commands.filterIsInstance<ControlMode>()
            .flatMapLatest(::handleCommand)
    }

    private suspend fun handleCommand(command: ControlMode): Flow<Controlling> {
        println("ControlActor.handleCommand: $command")
        return when (command) {
            is Accelerometer -> handleSubscribeToAccelerometerControlCommand(command)
            is Manual -> handleSendManualControlCommand(command)
        }
    }

    private fun handleSubscribeToAccelerometerControlCommand(command: Accelerometer): Flow<Controlling> {
        /*return controllerAccelerometerRepository.connect()
            .map(::send)*/

        return combine(
            controllerJoystickRepository.connect(),
            controllerJoystickTriggersRepository.connect()
        ) { joystick, joystickTriggers ->
            if (joystickTriggers.leftMotor != 0 || joystickTriggers.rightMotor != 0) {
                joystickTriggers
            } else {
                joystick
            }
        }.map(::send)
    }

    private suspend fun handleSendManualControlCommand(command: Manual): Flow<Controlling> {
        val data = command.motorsData
        return flowOf(send(data))
    }

    private suspend fun send(data: ControlMotorsData): Controlling {
        val isSucceed = bluetoothRepository.send(data.toConnectData())
        return if (isSucceed) Controlling.Succeed(data) else Controlling.Failed
    }

    private fun ControlMotorsData.toConnectData(): BluetoothOutputData {
        return BluetoothOutputData.ControlMotorsData(
            leftMotor = leftMotor,
            rightMotor = rightMotor
        )
    }
}