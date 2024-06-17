package magym.robobt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import magym.robobt.common.navigation.voyager.impl.NavigatorHolder
import magym.robobt.common.ui.theme.RoboTheme
import magym.robobt.feature.connect.ConnectScreenProvider
import org.koin.android.ext.android.inject

class AppActivity : ComponentActivity() {

    private val navigatorHolder: NavigatorHolder by inject()
    private val connectScreenProvider: ConnectScreenProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RoboTheme {
                UsualNavigator()
            }
        }
    }

    @Composable
    private fun UsualNavigator() {
        Navigator(
            screen = connectScreenProvider() as Screen,
            onBackPressed = { false },
        ) { navigator ->
            navigatorHolder.usualNavigator = navigator

            SlideTransition(navigator = navigator) { screen ->
                screen.Content()
            }
        }
    }
}