package magym.robobt.feature.control.presentation.tea

import magym.robobt.common.tea.dsl.DslReducer
import magym.robobt.feature.control.presentation.tea.core.ControlCommand
import magym.robobt.feature.control.presentation.tea.core.ControlCommand.ReadConnectionData
import magym.robobt.feature.control.presentation.tea.core.ControlEffect
import magym.robobt.feature.control.presentation.tea.core.ControlEvent
import magym.robobt.feature.control.presentation.tea.core.ControlEvent.ConnectionData
import magym.robobt.feature.control.presentation.tea.core.ControlEvent.Controlling
import magym.robobt.feature.control.presentation.tea.core.ControlNavigationCommand.Exit
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent.KeyboardAction
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent.OnBackPress
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent.OnChangeControlModeClick
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent.OnStart
import magym.robobt.feature.control.presentation.tea.model.ControlMode
import magym.robobt.feature.control.presentation.tea.model.ControlState

internal class ControlReducer : DslReducer<ControlCommand, ControlEffect, ControlEvent, ControlState>() {

    override fun reduce(event: ControlEvent) {
        when (event) {
            is ControlUiEvent -> reduceUi(event)
            is Controlling -> reduceControlling(event)
            is ConnectionData -> reduceConnectionDataReceived(event)
        }
    }

    private fun reduceUi(event: ControlUiEvent) = when (event) {
        is OnStart -> reduceOnStart()
        is OnBackPress -> reduceOnBackPress()
        is OnChangeControlModeClick -> reduceOnChangeControlModeClick()
        is KeyboardAction -> reduceKeyboardAction(event)
    }

    private fun reduceKeyboardAction(event: KeyboardAction) = when (event) {
        is KeyboardAction.OnTopLeftButtonDown -> commands(ControlCommand.KeyboardAction.OnTopLeftButtonDown)
        is KeyboardAction.OnTopLeftButtonUp -> commands(ControlCommand.KeyboardAction.OnTopLeftButtonUp)
        is KeyboardAction.OnTopRightButtonDown -> commands(ControlCommand.KeyboardAction.OnTopRightButtonDown)
        is KeyboardAction.OnTopRightButtonUp -> commands(ControlCommand.KeyboardAction.OnTopRightButtonUp)
        is KeyboardAction.OnBottomLeftButtonDown -> commands(ControlCommand.KeyboardAction.OnBottomLeftButtonDown)
        is KeyboardAction.OnBottomLeftButtonUp -> commands(ControlCommand.KeyboardAction.OnBottomLeftButtonUp)
        is KeyboardAction.OnBottomRightButtonDown -> commands(ControlCommand.KeyboardAction.OnBottomRightButtonDown)
        is KeyboardAction.OnBottomRightButtonUp -> commands(ControlCommand.KeyboardAction.OnBottomRightButtonUp)
    }

    private fun reduceOnStart() {
        commands(
            ControlCommand.ControlModeChanged(state.controlMode),
            ReadConnectionData.Subscribe
        )
    }

    private fun reduceOnChangeControlModeClick() {
        val controlMode = state.controlMode.next()
        state { copy(controlMode = controlMode) }
        commands(ControlCommand.ControlModeChanged(controlMode))
    }

    private fun reduceControlling(event: Controlling) = when (event) {
        is Controlling.Started -> Unit
        is Controlling.Succeed -> state { copy(motorsData = event.data) }
        is Controlling.Failed -> handleExit()
    }

    private fun reduceConnectionDataReceived(event: ConnectionData) {
        when (event) {
            is ConnectionData.Succeed -> state {
                copy(
                    temperature = event.data.temperature,
                    humidity = event.data.humidity,
                )
            }

            is ConnectionData.Failed -> handleExit()
        }
    }

    private fun reduceOnBackPress() {
        if (state.controlMode != ControlMode.Accelerometer) {
            state { copy(controlMode = ControlMode.Accelerometer) }
        } else {
            handleExit()
        }
    }

    private fun handleExit() {
        commands(Exit)
    }
}