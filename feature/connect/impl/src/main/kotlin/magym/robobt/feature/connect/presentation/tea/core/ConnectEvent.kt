package magym.robobt.feature.connect.presentation.tea.core

internal sealed interface ConnectEvent {

    sealed interface Connecting : ConnectEvent {
        data object Started : Connecting
        data object Succeed : Connecting
        class Failed(val error: Throwable) : Connecting
    }
}

internal sealed interface ConnectUiEvent : ConnectEvent {

    data object OnStart : ConnectUiEvent
}