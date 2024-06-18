package magym.robobt.feature.control.presentation.tea.core

internal sealed interface ControlEvent {

    sealed interface Controlling : ControlEvent {
        data object Started : Controlling
        data object Succeed : Controlling
        data object Failed : Controlling
    }
}

internal sealed interface ControlUiEvent : ControlEvent {

    data object OnStart : ControlUiEvent
}