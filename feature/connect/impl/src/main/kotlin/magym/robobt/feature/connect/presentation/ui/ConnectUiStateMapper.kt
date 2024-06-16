package magym.robobt.feature.connect.presentation.ui

import magym.robobt.common.pure.state.Lce
import magym.robobt.common.tea.component.UiStateMapper
import magym.robobt.feature.connect.presentation.tea.model.ConnectState
import magym.robobt.feature.connect.presentation.ui.state.ConnectUiState

internal class ConnectUiStateMapper : UiStateMapper<ConnectState, ConnectUiState> {

    override fun map(state: ConnectState): ConnectUiState {
        return ConnectUiState.Loading /*when () {
            is Lce.Loading -> {
                ConnectUiState.Loading
            }

            is Lce.Error -> {
                ConnectUiState.Error
            }
        }*/
    }
}