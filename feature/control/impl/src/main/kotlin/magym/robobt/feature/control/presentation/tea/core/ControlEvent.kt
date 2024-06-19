package magym.robobt.feature.control.presentation.tea.core

import magym.robobt.feature.control.presentation.tea.model.ControlMotorsData

internal sealed interface ControlEvent {

    sealed interface Controlling : ControlEvent {
        data object Started : Controlling
        data class Succeed(val data: ControlMotorsData) : Controlling
        data object Failed : Controlling
    }
}

internal sealed interface ControlUiEvent : ControlEvent {

    data object OnStart : ControlUiEvent
}