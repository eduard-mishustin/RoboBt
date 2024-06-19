package magym.robobt.feature.control.presentation.tea.actor

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import magym.robobt.common.tea.component.Actor
import magym.robobt.feature.control.presentation.tea.core.ControlCommand
import magym.robobt.feature.control.presentation.tea.core.ControlCommand.Control
import magym.robobt.feature.control.presentation.tea.core.ControlEvent
import magym.robobt.feature.control.presentation.tea.core.ControlEvent.Controlling
import magym.robobt.feature.control.presentation.tea.model.ControlMotorsData
import magym.robobt.repository.accelerometer.AccelerometerRepository
import magym.robobt.repository.connect.ConnectRepository

internal class ControlActor(
    private val connectRepository: ConnectRepository,
    private val accelerometerRepository: AccelerometerRepository,
    private val motorSpeedMapper: MotorSpeedMapper,
) : Actor<ControlCommand, ControlEvent> {

    override fun act(commands: Flow<ControlCommand>): Flow<ControlEvent> {
        return commands.filterIsInstance<Control>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: Control): Flow<Controlling> {
        return accelerometerRepository.connect()
            .map(motorSpeedMapper::map)
            .distinctUntilChanged()
            .map(::send)
            .onEach { delay(100) }
    }

    private fun send(data: ControlMotorsData): Controlling {
        val isSucceed = connectRepository.send(data.toConnectData())
        println("ControlActor: send data = $data, isSucceed = $isSucceed")
        return if (isSucceed) Controlling.Succeed(data) else Controlling.Failed
    }

    private fun ControlMotorsData.toConnectData(): String {
        return "m" + leftMotor + "z" + rightMotor + "z"
    }
}