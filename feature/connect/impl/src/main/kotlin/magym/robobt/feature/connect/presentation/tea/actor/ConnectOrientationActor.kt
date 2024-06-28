package magym.robobt.feature.connect.presentation.tea.actor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import magym.robobt.common.android.orientation.ScreenOrientationChanger
import magym.robobt.common.tea.component.Actor
import magym.robobt.feature.connect.presentation.tea.core.ConnectCommand
import magym.robobt.feature.connect.presentation.tea.core.ConnectCommand.ChangeOrientation
import magym.robobt.feature.connect.presentation.tea.core.ConnectEvent

internal class ConnectOrientationActor(
    private val screenOrientationChanger: ScreenOrientationChanger,
) : Actor<ConnectCommand, ConnectEvent> {

    override fun act(commands: Flow<ConnectCommand>): Flow<ConnectEvent> {
        return commands.filterIsInstance<ChangeOrientation>()
            .mapLatest(::handleCommand)
            .filterNotNull()
    }

    private fun handleCommand(command: ChangeOrientation): ConnectEvent? {
        screenOrientationChanger.change(command.orientation)
        return null
    }
}