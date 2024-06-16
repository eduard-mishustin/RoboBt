package magym.robobt.feature.control.presentation.tea

import magym.robobt.common.tea.component.Actor
import magym.robobt.common.tea.impl.ScreenModelStore
import magym.robobt.feature.control.presentation.tea.core.ControlCommand
import magym.robobt.feature.control.presentation.tea.core.ControlEffect
import magym.robobt.feature.control.presentation.tea.core.ControlEvent
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent
import magym.robobt.feature.control.presentation.tea.model.ControlState
import magym.robobt.feature.control.presentation.ui.ControlUiStateMapper
import magym.robobt.feature.control.presentation.ui.state.ControlUiState

internal class ControlStore(
    reducer: ControlReducer,
    uiStateMapper: ControlUiStateMapper,
    actors: Set<Actor<ControlCommand, ControlEvent>>,
) : ScreenModelStore<ControlCommand, ControlEffect, ControlEvent, ControlUiEvent, ControlState, ControlUiState>(
    initialState = ControlState(),
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
)