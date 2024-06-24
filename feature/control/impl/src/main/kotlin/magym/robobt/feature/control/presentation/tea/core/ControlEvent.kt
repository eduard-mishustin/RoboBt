package magym.robobt.feature.control.presentation.tea.core

import magym.robobt.feature.control.presentation.tea.model.ControlMotorsData
import magym.robobt.repository.connect.bluetooth.model.BluetoothInputData

internal sealed interface ControlEvent {

    sealed interface Controlling : ControlEvent {

        data object Started : Controlling
        data class Succeed(val data: ControlMotorsData) : Controlling
        data object Failed : Controlling
    }

    sealed interface ConnectionData : ControlEvent {

        data class Succeed(val data: BluetoothInputData) : ConnectionData
        data object Failed : ConnectionData
    }
}

internal sealed interface ControlUiEvent : ControlEvent {

    data object OnStart : ControlUiEvent

    data object OnChangeControlModeClick : ControlUiEvent

    data object OnTopLeftButtonDown : ControlUiEvent
    data object OnTopLeftButtonUp : ControlUiEvent
    data object OnTopRightButtonDown : ControlUiEvent
    data object OnTopRightButtonUp : ControlUiEvent
    data object OnBottomLeftButtonDown : ControlUiEvent
    data object OnBottomLeftButtonUp : ControlUiEvent
    data object OnBottomRightButtonDown : ControlUiEvent
    data object OnBottomRightButtonUp : ControlUiEvent
}