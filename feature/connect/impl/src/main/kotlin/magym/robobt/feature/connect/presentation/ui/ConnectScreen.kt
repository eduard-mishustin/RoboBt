package magym.robobt.feature.connect.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import magym.robobt.common.ui.theme.RoboTheme
import magym.robobt.common.ui.theme.ScreenPreview
import magym.robobt.feature.connect.presentation.ui.state.ConnectUiState

@Composable
internal fun ConnectScreen(
    state: ConnectUiState,
) {
    when (state) {
        is ConnectUiState.Loading -> Loading()
        is ConnectUiState.Empty -> Empty()
        is ConnectUiState.Error -> Error()
    }
}

@Composable
private fun Loading() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Empty() {
    Text(text = "Empty")
}

@Composable
private fun Error() {
    Text(text = "Error")
}

@ScreenPreview
@Composable
private fun ConnectScreenPreview() = RoboTheme {
    ConnectScreen(ConnectUiState.Loading)
}
