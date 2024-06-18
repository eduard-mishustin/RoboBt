package magym.robobt.feature.control.presentation.tea

import magym.robobt.common.tea.dsl.DslReducer
import magym.robobt.feature.control.presentation.tea.core.ControlCommand
import magym.robobt.feature.control.presentation.tea.core.ControlCommand.Control
import magym.robobt.feature.control.presentation.tea.core.ControlEffect
import magym.robobt.feature.control.presentation.tea.core.ControlEvent
import magym.robobt.feature.control.presentation.tea.core.ControlEvent.Controlling
import magym.robobt.feature.control.presentation.tea.core.ControlNavigationCommand.Exit
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent.OnStart
import magym.robobt.feature.control.presentation.tea.model.ControlState

internal class ControlReducer : DslReducer<ControlCommand, ControlEffect, ControlEvent, ControlState>() {

    override fun reduce(event: ControlEvent) = when (event) {
        is ControlUiEvent -> reduceUi(event)
        is Controlling -> reduceControlling(event)
    }

    private fun reduceUi(event: ControlUiEvent) = when (event) {
        is OnStart -> commands(Control)
    }

    private fun reduceControlling(event: Controlling) = when (event) {
        is Controlling.Started -> Unit
        is Controlling.Succeed -> Unit
        is Controlling.Failed -> commands(Exit)
    }
}