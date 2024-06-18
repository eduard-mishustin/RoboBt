package magym.robobt.feature.control.presentation.tea.actor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import magym.robobt.common.navigation.api.Router
import magym.robobt.common.tea.component.Actor
import magym.robobt.feature.control.presentation.tea.core.ControlCommand
import magym.robobt.feature.control.presentation.tea.core.ControlEvent
import magym.robobt.feature.control.presentation.tea.core.ControlNavigationCommand
import magym.robobt.feature.control.presentation.tea.core.ControlNavigationCommand.Exit

internal class ControlNavigationActor(
    private val router: Router,
) : Actor<ControlCommand, ControlEvent> {

    override fun act(commands: Flow<ControlCommand>): Flow<ControlEvent> {
        return commands.filterIsInstance<ControlNavigationCommand>()
            .mapLatest(::handleCommand)
            .filterNotNull()
    }

    private fun handleCommand(command: ControlNavigationCommand): ControlEvent? = with(router) {
        when (command) {
            is Exit -> exit()
        }

        return null
    }
}