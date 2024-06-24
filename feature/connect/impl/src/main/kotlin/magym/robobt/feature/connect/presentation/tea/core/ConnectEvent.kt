package magym.robobt.feature.connect.presentation.tea.core

import magym.robobt.repository.connect.bluetooth.model.BluetoothResult

internal sealed interface ConnectEvent {

    sealed interface Connecting : ConnectEvent {
        data object Started : Connecting
        data object Succeed : Connecting
        class Failed(val error: BluetoothResult) : Connecting
    }
}

internal sealed interface ConnectUiEvent : ConnectEvent {

    data object OnStart : ConnectUiEvent
}