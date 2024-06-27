package magym.robobt.feature.connect.presentation.tea.actor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import magym.robobt.common.pure.util.startWith
import magym.robobt.common.tea.component.Actor
import magym.robobt.feature.connect.presentation.tea.core.ConnectCommand
import magym.robobt.feature.connect.presentation.tea.core.ConnectCommand.Connect
import magym.robobt.feature.connect.presentation.tea.core.ConnectEvent
import magym.robobt.feature.connect.presentation.tea.core.ConnectEvent.Connecting
import magym.robobt.repository.connect.bluetooth.BluetoothRepository
import magym.robobt.repository.connect.bluetooth.model.BluetoothConnectResult

internal class ConnectActor(
    private val repository: BluetoothRepository,
) : Actor<ConnectCommand, ConnectEvent> {

    override fun act(commands: Flow<ConnectCommand>): Flow<ConnectEvent> {
        return commands.filterIsInstance<Connect>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: Connect): Flow<Connecting> {
        return repository.connect()
            .map {
                if (it is BluetoothConnectResult.Success) Connecting.Succeed
                else Connecting.Failed(it)
            }
            .startWith(Connecting.Started)
    }
}