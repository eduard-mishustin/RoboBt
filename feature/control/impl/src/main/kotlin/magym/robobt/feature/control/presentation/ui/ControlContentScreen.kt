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
import magym.robobt.feature.video_stream.VideoStreamComponent

internal class ControlContentScreen : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<ControlStore>()) { state ->
        LifecycleEffect(onStarted = acceptable(OnStart))
        BackHandlerWithLifecycle { accept(OnBackPress) }

        VideoStreamComponent()

        ControlScreen(
            state = state,
            onChangeControlModeClick = acceptable(ControlUiEvent.OnChangeControlModeClick),
            onTopLeftButtonDown = acceptable(ControlUiEvent.KeyboardAction.OnTopLeftButtonDown),
            onTopLeftButtonUp = acceptable(ControlUiEvent.KeyboardAction.OnTopLeftButtonUp),
            onTopRightButtonDown = acceptable(ControlUiEvent.KeyboardAction.OnTopRightButtonDown),
            onTopRightButtonUp = acceptable(ControlUiEvent.KeyboardAction.OnTopRightButtonUp),
            onBottomLeftButtonDown = acceptable(ControlUiEvent.KeyboardAction.OnBottomLeftButtonDown),
            onBottomLeftButtonUp = acceptable(ControlUiEvent.KeyboardAction.OnBottomLeftButtonUp),
            onBottomRightButtonDown = acceptable(ControlUiEvent.KeyboardAction.OnBottomRightButtonDown),
            onBottomRightButtonUp = acceptable(ControlUiEvent.KeyboardAction.OnBottomRightButtonUp),
        )
    }
}