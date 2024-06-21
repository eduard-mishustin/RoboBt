package magym.robobt.feature.control.presentation.tea

import magym.robobt.common.tea.dsl.DslReducer
import magym.robobt.feature.control.presentation.tea.core.ControlCommand
import magym.robobt.feature.control.presentation.tea.core.ControlEffect
import magym.robobt.feature.control.presentation.tea.core.ControlEvent
import magym.robobt.feature.control.presentation.tea.core.ControlEvent.Controlling
import magym.robobt.feature.control.presentation.tea.core.ControlNavigationCommand.Exit
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent.OnBottomLeftButtonDown
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent.OnBottomLeftButtonUp
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent.OnBottomRightButtonDown
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent.OnBottomRightButtonUp
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent.OnChangeControlModeClick
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent.OnStart
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent.OnTopLeftButtonDown
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent.OnTopLeftButtonUp
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent.OnTopRightButtonDown
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent.OnTopRightButtonUp
import magym.robobt.feature.control.presentation.tea.model.ControlMode
import magym.robobt.feature.control.presentation.tea.model.ControlOrientation
import magym.robobt.feature.control.presentation.tea.model.ControlState

internal class ControlReducer : DslReducer<ControlCommand, ControlEffect, ControlEvent, ControlState>() {

    override fun reduce(event: ControlEvent) {
        when (event) {
            is ControlUiEvent -> reduceUi(event)
            is Controlling -> reduceControlling(event)
        }
    }

    private fun reduceUi(event: ControlUiEvent) = when (event) {
        is OnStart -> reduceOnStart()
        is OnChangeControlModeClick -> reduceOnChangeControlModeClick()

        is OnTopLeftButtonDown -> commands(ControlCommand.ControlMode.Manual(state.motorsData.copy(rightMotor = 255)))
        is OnTopLeftButtonUp -> commands(ControlCommand.ControlMode.Manual(state.motorsData.copy(rightMotor = 0)))
        is OnTopRightButtonDown -> commands(ControlCommand.ControlMode.Manual(state.motorsData.copy(leftMotor = 255)))
        is OnTopRightButtonUp -> commands(ControlCommand.ControlMode.Manual(state.motorsData.copy(leftMotor = 0)))
        is OnBottomLeftButtonDown -> commands(ControlCommand.ControlMode.Manual(state.motorsData.copy(rightMotor = -255)))
        is OnBottomLeftButtonUp -> commands(ControlCommand.ControlMode.Manual(state.motorsData.copy(rightMotor = 0)))
        is OnBottomRightButtonDown -> commands(ControlCommand.ControlMode.Manual(state.motorsData.copy(leftMotor = -255)))
        is OnBottomRightButtonUp -> commands(ControlCommand.ControlMode.Manual(state.motorsData.copy(leftMotor = 0)))
    }

    private fun reduceOnStart() {
        val command =
            if (state.controlMode == ControlMode.Accelerometer) ControlCommand.ControlMode.Accelerometer
            else ControlCommand.ControlMode.Manual(state.motorsData)

        commands(command)
    }

    private fun reduceOnChangeControlModeClick() {
        state { copy(controlMode = ControlMode.Manual) }
        effects(ControlEffect.ChangeControlOrientation(ControlOrientation.Landscape))
    }

    private fun reduceControlling(event: Controlling) = when (event) {
        is Controlling.Started -> Unit
        is Controlling.Succeed -> state { copy(motorsData = event.data) }
        is Controlling.Failed -> commands(Exit)
    }
}