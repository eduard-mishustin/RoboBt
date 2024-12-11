package magym.robobt

import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import magym.robobt.common.android.SingleActivityHolder
import magym.robobt.common.navigation.voyager.impl.NavigatorHolder
import magym.robobt.common.ui.theme.RoboTheme
import magym.robobt.feature.connect.ConnectScreenProvider
import magym.robobt.video_stream.VideoStreamRepository
import org.koin.android.ext.android.inject
import java.lang.ref.WeakReference

class AppActivity : ComponentActivity() {

    private val singleActivityHolder: SingleActivityHolder by inject()
    private val navigatorHolder: NavigatorHolder by inject()
    private val connectScreenProvider: ConnectScreenProvider by inject()
    private val activityGenericMotionDelegate: ActivityGenericMotionDelegate by inject()
    private val videoStreamRepository: VideoStreamRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        singleActivityHolder.activity = WeakReference(this)

        enableEdgeToEdge()

        setContent {
            RoboTheme {
                UsualNavigator()
            }
        }
    }

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        val result = activityGenericMotionDelegate.onGenericMotionEvent(event)
        return if (result) true else super.onGenericMotionEvent(event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val result = activityGenericMotionDelegate.onKeyDown(keyCode, event)
        return if (result) true else super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        val result = activityGenericMotionDelegate.onKeyUp(keyCode, event)
        return if (result) true else super.onKeyUp(keyCode, event)
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
            MJPEGStreamScreen()
        }
    }

    @Composable
    fun MJPEGStreamScreen() {
        var currentFrame by remember { mutableStateOf<ImageBitmap?>(null) }

        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                videoStreamRepository.connect { bitmap ->
                    currentFrame = bitmap.asImageBitmap()
                }
            }
        }

        if (currentFrame != null) {
            Image(
                bitmap = currentFrame!!,
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}