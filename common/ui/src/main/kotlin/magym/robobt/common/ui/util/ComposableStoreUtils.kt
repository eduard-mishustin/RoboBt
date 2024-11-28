package magym.robobt.common.ui.util

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import magym.robobt.common.tea.impl.ScreenModelStore
import org.koin.core.parameter.parametersOf

@Composable
inline fun <reified T : ScreenModelStore<*, *, *, *, *, *>> Screen.store(params: Any? = null): T {
    return getScreenModel { parametersOf(params) }
}