package magym.robobt.feature.connect.presentation.tea

import magym.robobt.common.tea.dsl.DslReducer
import magym.robobt.feature.connect.presentation.tea.core.ConnectCommand
import magym.robobt.feature.connect.presentation.tea.core.ConnectCommand.Connect
import magym.robobt.feature.connect.presentation.tea.core.ConnectEffect
import magym.robobt.feature.connect.presentation.tea.core.ConnectEvent
import magym.robobt.feature.connect.presentation.tea.core.ConnectEvent.Connecting
import magym.robobt.feature.connect.presentation.tea.core.ConnectUiEvent
import magym.robobt.feature.connect.presentation.tea.core.ConnectUiEvent.OnStart
import magym.robobt.feature.connect.presentation.tea.model.ConnectState

internal class ConnectReducer : DslReducer<ConnectCommand, ConnectEffect, ConnectEvent, ConnectState>() {

    override fun reduce(event: ConnectEvent) = when (event) {
        is ConnectUiEvent -> reduceUi(event)
        is Connecting -> reduceConnecting(event)
    }

    private fun reduceUi(event: ConnectUiEvent) = when (event) {
        is OnStart -> commands(Connect)
    }

    private fun reduceConnecting(event: Connecting) = when (event) {
        is Connecting.Started -> Unit
        is Connecting.Succeed -> Unit
        is Connecting.Failed -> Unit
    }
}