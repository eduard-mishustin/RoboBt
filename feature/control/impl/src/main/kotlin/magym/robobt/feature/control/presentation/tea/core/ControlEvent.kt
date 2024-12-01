package magym.robobt.feature.control.presentation.tea.core

import magym.robobt.common.pure.model.ControlMotorsData
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

    data object OnBackPress : ControlUiEvent

    data object OnChangeControlModeClick : ControlUiEvent

    sealed interface KeyboardAction : ControlUiEvent {

        data object OnTopLeftButtonDown : KeyboardAction

        data object OnTopLeftButtonUp : KeyboardAction

        data object OnTopRightButtonDown : KeyboardAction

        data object OnTopRightButtonUp : KeyboardAction

        data object OnBottomLeftButtonDown : KeyboardAction

        data object OnBottomLeftButtonUp : KeyboardAction

        data object OnBottomRightButtonDown : KeyboardAction

        data object OnBottomRightButtonUp : KeyboardAction
    }
}