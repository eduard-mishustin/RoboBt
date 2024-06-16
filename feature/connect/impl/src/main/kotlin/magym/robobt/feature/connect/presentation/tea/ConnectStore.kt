package magym.robobt.feature.connect.presentation.tea

import magym.robobt.common.tea.component.Actor
import magym.robobt.common.tea.impl.ScreenModelStore
import magym.robobt.feature.connect.presentation.tea.core.ConnectCommand
import magym.robobt.feature.connect.presentation.tea.core.ConnectEffect
import magym.robobt.feature.connect.presentation.tea.core.ConnectEvent
import magym.robobt.feature.connect.presentation.tea.core.ConnectUiEvent
import magym.robobt.feature.connect.presentation.tea.model.ConnectState
import magym.robobt.feature.connect.presentation.ui.ConnectUiStateMapper
import magym.robobt.feature.connect.presentation.ui.state.ConnectUiState

internal class ConnectStore(
    reducer: ConnectReducer,
    uiStateMapper: ConnectUiStateMapper,
    actors: Set<Actor<ConnectCommand, ConnectEvent>>,
) : ScreenModelStore<ConnectCommand, ConnectEffect, ConnectEvent, ConnectUiEvent, ConnectState, ConnectUiState>(
    initialState = ConnectState(),
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
)