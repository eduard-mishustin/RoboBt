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
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent.OnBackPress
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
import magym.robobt.feature.control.presentation.tea.model.ControlState
import magym.robobt.repository.accelerometer.model.ControlMotorsData

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

        is OnTopLeftButtonDown -> onButtonClicked(state.motorsData.copy(rightMotor = 255))
        is OnTopLeftButtonUp -> onButtonClicked(state.motorsData.copy(rightMotor = 0))
        is OnTopRightButtonDown -> onButtonClicked(state.motorsData.copy(leftMotor = 255))
        is OnTopRightButtonUp -> onButtonClicked(state.motorsData.copy(leftMotor = 0))
        is OnBottomLeftButtonDown -> onButtonClicked(state.motorsData.copy(rightMotor = -255))
        is OnBottomLeftButtonUp -> onButtonClicked(state.motorsData.copy(rightMotor = 0))
        is OnBottomRightButtonDown -> onButtonClicked(state.motorsData.copy(leftMotor = -255))
        is OnBottomRightButtonUp -> onButtonClicked(state.motorsData.copy(leftMotor = 0))
    }

    private fun onButtonClicked(motorsData: ControlMotorsData) {
        state { copy(motorsData = motorsData) }
        commands(ControlCommand.ControlMode.Manual(motorsData))
    }

    private fun reduceOnStart() {
        val command =
            if (state.controlMode == ControlMode.Accelerometer) ControlCommand.ControlMode.Accelerometer
            else ControlCommand.ControlMode.Manual(state.motorsData)

        commands(command, ReadConnectionData.Subscribe)
    }

    private fun reduceOnChangeControlModeClick() {
        state { copy(controlMode = ControlMode.Manual) }
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