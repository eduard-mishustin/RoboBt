package magym.robobt.feature.connect.presentation.tea.actor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import magym.robobt.common.navigation.api.Router
import magym.robobt.common.tea.component.Actor
import magym.robobt.feature.connect.presentation.tea.core.ConnectCommand
import magym.robobt.feature.connect.presentation.tea.core.ConnectEvent
import magym.robobt.feature.connect.presentation.tea.core.ConnectNavigationCommand
import magym.robobt.feature.connect.presentation.tea.core.ConnectNavigationCommand.OpenControl
import magym.robobt.feature.control.ControlScreenProvider

internal class ConnectNavigationActor(
    private val router: Router,
    private val controlScreenProvider: ControlScreenProvider,
) : Actor<ConnectCommand, ConnectEvent> {

    override fun act(commands: Flow<ConnectCommand>): Flow<ConnectEvent> {
        return commands.filterIsInstance<ConnectNavigationCommand>()
            .mapLatest(::handleCommand)
            .filterNotNull()
    }

    private fun handleCommand(command: ConnectNavigationCommand): ConnectEvent? = with(router) {
        when (command) {
            is OpenControl -> {
                navigate(controlScreenProvider())
            }
        }

        return null
    }
}