package magym.robobt.common.ui.util

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen

/**
 * Fixes the bug when BackHandler doesn't work after process being restarted
 */
@Composable
fun Screen.BackHandlerWithLifecycle(
    enabled: Boolean = true,
    onBack: () -> Unit
) {
    var refreshBackHandler by rememberSaveable { mutableStateOf(false) }

    LifecycleEffect(
        onStarted = { refreshBackHandler = true },
        onDisposed = { refreshBackHandler = false },
    )

    if (refreshBackHandler) {
        BackHandler(enabled = enabled) {
            onBack()
        }
    }
}