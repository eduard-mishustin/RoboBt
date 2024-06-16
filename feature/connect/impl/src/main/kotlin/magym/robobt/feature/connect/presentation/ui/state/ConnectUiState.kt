package magym.robobt.feature.connect.presentation.ui.state

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface ConnectUiState {

    data object Loading : ConnectUiState

    data object Empty : ConnectUiState

    data object Error : ConnectUiState
}