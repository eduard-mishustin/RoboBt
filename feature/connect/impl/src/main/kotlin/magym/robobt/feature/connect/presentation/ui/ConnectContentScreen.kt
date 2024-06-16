package magym.robobt.feature.connect.presentation.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import magym.robobt.common.navigation.voyager.BaseScreen
import magym.robobt.common.tea.compose.TeaComposable
import magym.robobt.common.tea.compose.acceptable
import magym.robobt.common.ui.util.store
import magym.robobt.feature.connect.presentation.tea.ConnectStore
import magym.robobt.feature.connect.presentation.tea.core.ConnectUiEvent.OnStart

internal class ConnectContentScreen : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<ConnectStore>()) { state ->
        LifecycleEffect(
            onStarted = acceptable(OnStart),
        )

        ConnectScreen(
            state = state,
        )
    }
}