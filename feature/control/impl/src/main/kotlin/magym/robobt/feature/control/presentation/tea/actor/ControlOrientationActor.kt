package magym.robobt.feature.control.presentation.tea.actor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import magym.robobt.common.android.orientation.ScreenOrientationChanger
import magym.robobt.common.tea.component.Actor
import magym.robobt.feature.control.presentation.tea.core.ControlCommand
import magym.robobt.feature.control.presentation.tea.core.ControlCommand.ChangeOrientation
import magym.robobt.feature.control.presentation.tea.core.ControlEvent

internal class ControlOrientationActor(
    private val screenOrientationChanger: ScreenOrientationChanger,
) : Actor<ControlCommand, ControlEvent> {

    override fun act(commands: Flow<ControlCommand>): Flow<ControlEvent> {
        return commands.filterIsInstance<ChangeOrientation>()
            .mapLatest(::handleCommand)
            .filterNotNull()
    }

    private fun handleCommand(command: ChangeOrientation): ControlEvent? {
        screenOrientationChanger.change(command.orientation)
        return null
    }
}