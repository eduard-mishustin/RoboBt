package magym.robobt.feature.control.presentation.tea.actor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import magym.robobt.common.tea.component.Actor
import magym.robobt.controller.ControlMotorsData
import magym.robobt.controller.accelerometer.ControllerAccelerometerRepository
import magym.robobt.controller.joystick.ControllerJoystickRepository
import magym.robobt.controller.joystick_triggers.ControllerJoystickTriggersRepository
import magym.robobt.controller.keyboard.ControllerKeyboardRepository
import magym.robobt.feature.control.presentation.tea.core.ControlCommand
import magym.robobt.feature.control.presentation.tea.core.ControlCommand.ControlModeChanged
import magym.robobt.feature.control.presentation.tea.core.ControlEvent
import magym.robobt.feature.control.presentation.tea.core.ControlEvent.Controlling
import magym.robobt.feature.control.presentation.tea.model.ControlMode
import magym.robobt.repository.connect.bluetooth.BluetoothRepository
import magym.robobt.repository.connect.bluetooth.model.BluetoothOutputData

internal class ControlActor(
    private val bluetoothRepository: BluetoothRepository,
    private val keyboardRepository: ControllerKeyboardRepository,
    private val accelerometerRepository: ControllerAccelerometerRepository,
    private val joystickRepository: ControllerJoystickRepository,
    private val joystickTriggersRepository: ControllerJoystickTriggersRepository,
) : Actor<ControlCommand, ControlEvent> {

    override fun act(commands: Flow<ControlCommand>): Flow<ControlEvent> {
        return commands.filterIsInstance<ControlModeChanged>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: ControlModeChanged): Flow<Controlling> {
        println("ControlActor.handleCommand: $command")
        return when (command.mode) {
            ControlMode.Manual -> handleSendManualControlCommand()
            ControlMode.Accelerometer -> handleSubscribeToAccelerometerControlCommand()
        }
    }

    private fun handleSendManualControlCommand(): Flow<Controlling> {
        return combine(
            keyboardRepository.connect(),
            joystickRepository.connect(),
            joystickTriggersRepository.connect()
        ) { keyboard, joystick, joystickTriggers ->
            when {
                keyboard.isNotEmpty -> keyboard
                joystickTriggers.isNotEmpty -> joystickTriggers
                else -> joystick
            }
        }.map(::send)
    }

    private fun handleSubscribeToAccelerometerControlCommand(): Flow<Controlling> {
        return accelerometerRepository.connect()
            .map(::send)
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