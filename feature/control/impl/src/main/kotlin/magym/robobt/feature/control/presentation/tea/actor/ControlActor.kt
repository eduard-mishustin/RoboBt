package magym.robobt.feature.control.presentation.tea.actor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import magym.robobt.common.tea.component.Actor
import magym.robobt.feature.control.presentation.tea.core.ControlCommand
import magym.robobt.feature.control.presentation.tea.core.ControlCommand.Control
import magym.robobt.feature.control.presentation.tea.core.ControlEvent
import magym.robobt.feature.control.presentation.tea.core.ControlEvent.Controlling

internal class ControlActor(
) : Actor<ControlCommand, ControlEvent> {

    override fun act(commands: Flow<ControlCommand>): Flow<ControlEvent> {
        return commands.filterIsInstance<Control>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: Control): Flow<Controlling> {
        return flowOf(Controlling.Started)/*repository.control()
            .map(ReviewsLoading::Succeed)
            .startWith(ReviewsLoading.Started)
            .onCatchReturn(ReviewsLoading::Failed)*/
    }
}