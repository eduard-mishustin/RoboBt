package magym.robobt.feature.control.presentation.tea.actor

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import magym.robobt.common.tea.component.Actor
import magym.robobt.feature.control.presentation.tea.core.ControlCommand
import magym.robobt.feature.control.presentation.tea.core.ControlCommand.ControlMode
import magym.robobt.feature.control.presentation.tea.core.ControlCommand.ControlMode.Accelerometer
import magym.robobt.feature.control.presentation.tea.core.ControlCommand.ControlMode.Manual
import magym.robobt.feature.control.presentation.tea.core.ControlEvent
import magym.robobt.feature.control.presentation.tea.core.ControlEvent.Controlling
import magym.robobt.repository.accelerometer.AccelerometerRepository
import magym.robobt.repository.accelerometer.MotorSpeedMapper
import magym.robobt.repository.accelerometer.model.ControlMotorsData
import magym.robobt.repository.connect.bluetooth.BluetoothRepository
import magym.robobt.repository.connect.bluetooth.model.BluetoothOutputData

internal class ControlActor(
    private val bluetoothRepository: BluetoothRepository,
    private val accelerometerRepository: AccelerometerRepository,
    private val motorSpeedMapper: MotorSpeedMapper,
) : Actor<ControlCommand, ControlEvent> {

    override fun act(commands: Flow<ControlCommand>): Flow<ControlEvent> {
        return commands.filterIsInstance<ControlMode>()
            .flatMapLatest(::handleCommand)
    }

    private suspend fun handleCommand(command: ControlMode): Flow<Controlling> {
        return when (command) {
            is Accelerometer -> handleSubscribeToAccelerometerControlCommand(command)
            is Manual -> handleSendManualControlCommand(command)
        }
    }

    private fun handleSubscribeToAccelerometerControlCommand(command: Accelerometer): Flow<Controlling> {
        return accelerometerRepository.connect()
            .map(motorSpeedMapper::map)
            .distinctUntilChanged()
            .map(::send)
            .onEach { delay(100) }
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