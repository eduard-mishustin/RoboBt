package magym.robobt.feature.control.presentation.ui

import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import magym.robobt.common.android.SingleActivityHolder
import magym.robobt.common.navigation.voyager.BaseScreen
import magym.robobt.common.tea.compose.TeaComposable
import magym.robobt.common.tea.compose.acceptable
import magym.robobt.common.ui.util.store
import magym.robobt.feature.control.presentation.tea.ControlStore
import magym.robobt.feature.control.presentation.tea.core.ControlEffect.ChangeControlOrientation
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent
import magym.robobt.feature.control.presentation.tea.core.ControlUiEvent.OnStart
import org.koin.core.component.inject

internal class ControlContentScreen : BaseScreen() {

    private val singleActivityHolder: SingleActivityHolder by inject()

    @Composable
    override fun Screen() = TeaComposable(store<ControlStore>()) { state ->
        LifecycleEffect(
            onStarted = acceptable(OnStart),
        )

        EffectHandler { effect ->
            when (effect) {
                is ChangeControlOrientation -> {
                    singleActivityHolder.requireActivity().requestedOrientation = SCREEN_ORIENTATION_LANDSCAPE
                }
            }
        }

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