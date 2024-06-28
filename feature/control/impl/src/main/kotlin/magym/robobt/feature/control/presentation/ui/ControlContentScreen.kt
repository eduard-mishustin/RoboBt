package magym.robobt.feature.control.presentation.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import magym.robobt.common.navigation.voyager.BaseScreen
import magym.robobt.common.tea.compose.TeaComposable
import magym.robobt.common.tea.compose.acceptable
import magym.robobt.common.ui.util.BackHandlerWithLifecycle
import magym.robobt.common.ui.util.store
import magym.robobt.feature.control.presentation.tea.ControlStore
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent.OnBackPress
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent.OnStart

internal class ControlContentScreen : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<ControlStore>()) { state ->
        LifecycleEffect(onStarted = acceptable(OnStart))
        BackHandlerWithLifecycle { accept(OnBackPress) }

        ControlScreen(
            state = state,
            onChangeControlModeClick = acceptable(ControlUiEvent.OnChangeControlModeClick),
            onTopLeftButtonDown = acceptable(ControlUiEvent.OnTopLeftButtonDown),
            onTopLeftButtonUp = acceptable(ControlUiEvent.OnTopLeftButtonUp),
            onTopRightButtonDown = acceptable(ControlUiEvent.OnTopRightButtonDown),
            onTopRightButtonUp = acceptable(ControlUiEvent.OnTopRightButtonUp),
            onBottomLeftButtonDown = acceptable(ControlUiEvent.OnBottomLeftButtonDown),
            onBottomLeftButtonUp = acceptable(ControlUiEvent.OnBottomLeftButtonUp),
            onBottomRightButtonDown = acceptable(ControlUiEvent.OnBottomRightButtonDown),
            onBottomRightButtonUp = acceptable(ControlUiEvent.OnBottomRightButtonUp),
        )
    }
}