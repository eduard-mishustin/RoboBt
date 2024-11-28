package magym.robobt.common.navigation.voyager

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import magym.robobt.common.navigation.api.RoboScreen

abstract class BaseScreen : Screen, RoboScreen {

    override val key: ScreenKey = uniqueScreenKey

    @Composable
    final override fun Content() {
        Screen()
    }

    @Composable
    abstract fun Screen()
}