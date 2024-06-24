package magym.robobt.feature.control.presentation.tea.actor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import magym.robobt.common.tea.component.Actor
import magym.robobt.feature.control.presentation.tea.core.ControlCommand
import magym.robobt.feature.control.presentation.tea.core.ControlCommand.ReadConnectionData
import magym.robobt.feature.control.presentation.tea.core.ControlEvent
import magym.robobt.feature.control.presentation.tea.core.ControlEvent.ConnectionData
import magym.robobt.repository.connect.bluetooth.BluetoothRepository

internal class ControlReadConnectionDataActor(
    private val bluetoothRepository: BluetoothRepository,
) : Actor<ControlCommand, ControlEvent> {

    override fun act(commands: Flow<ControlCommand>): Flow<ControlEvent> {
        return commands.filterIsInstance<ReadConnectionData>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: ReadConnectionData): Flow<ConnectionData> {
        return when (command) {
            is ReadConnectionData.Subscribe -> handleSubscribeCommand()
            is ReadConnectionData.Unsubscribe -> emptyFlow()
        }
    }

    private fun handleSubscribeCommand(): Flow<ConnectionData> {
        return bluetoothRepository.read()
            .map { data ->
                if (data != null) ConnectionData.Succeed(data)
                else ConnectionData.Failed
            }
    }
}