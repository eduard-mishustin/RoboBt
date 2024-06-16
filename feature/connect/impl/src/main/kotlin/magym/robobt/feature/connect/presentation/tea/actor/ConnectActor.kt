package magym.robobt.feature.connect.presentation.tea.actor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import magym.robobt.common.tea.component.Actor
import magym.robobt.feature.connect.presentation.tea.core.ConnectCommand
import magym.robobt.feature.connect.presentation.tea.core.ConnectCommand.Connect
import magym.robobt.feature.connect.presentation.tea.core.ConnectEvent
import magym.robobt.feature.connect.presentation.tea.core.ConnectEvent.Connecting

internal class ConnectActor(
) : Actor<ConnectCommand, ConnectEvent> {

    override fun act(commands: Flow<ConnectCommand>): Flow<ConnectEvent> {
        return commands.filterIsInstance<Connect>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: Connect): Flow<Connecting> {
        return flowOf(Connecting.Started)/*repository.connect()
            .map(::Succeed)
            .startWith(.Started)
            .onCatchReturn(::Failed)*/
    }
}