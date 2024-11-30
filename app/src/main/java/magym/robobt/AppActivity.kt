package magym.robobt

import android.os.Bundle
import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import magym.robobt.common.android.SingleActivityHolder
import magym.robobt.common.navigation.voyager.impl.NavigatorHolder
import magym.robobt.common.ui.theme.RoboTheme
import magym.robobt.controller.joystick_triggers.MutableControllerJoystickTriggersRepository
import magym.robobt.feature.connect.ConnectScreenProvider
import magym.robobt.repository.input_device.InputDeviceData
import magym.robobt.repository.input_device.joystick.MutableJoystickRepository
import org.koin.android.ext.android.inject
import java.lang.ref.WeakReference

class AppActivity : ComponentActivity() {

    private val singleActivityHolder: SingleActivityHolder by inject()
    private val navigatorHolder: NavigatorHolder by inject()
    private val connectScreenProvider: ConnectScreenProvider by inject()
    private val joystickRepository: MutableJoystickRepository by inject()
    private val joystickTriggersRepository: MutableControllerJoystickTriggersRepository by inject()

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
        val device = event.device

        if (device != null && device.sources and InputDevice.SOURCE_JOYSTICK != 0) {
            val leftTrigger = event.getAxisValue(MotionEvent.AXIS_LTRIGGER)
            val rightTrigger = event.getAxisValue(MotionEvent.AXIS_RTRIGGER)
            joystickTriggersRepository.onTriggerInputChanged(leftTrigger, rightTrigger)

            val xLeftStick = event.getAxisValue(MotionEvent.AXIS_X, 0)
            val yLeftStick = event.getAxisValue(MotionEvent.AXIS_Y, 0)
            joystickRepository.onStickInputChanged(xLeftStick, yLeftStick)
            return true
        }

        return super.onGenericMotionEvent(event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BUTTON_L1 -> {
                joystickTriggersRepository.onLeftButtonDown()
                return true
            }

            KeyEvent.KEYCODE_BUTTON_R1 -> {
                joystickTriggersRepository.onRightButtonDown()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BUTTON_L1 -> {
                joystickTriggersRepository.onLeftButtonUp()
                return true
            }

            KeyEvent.KEYCODE_BUTTON_R1 -> {
                joystickTriggersRepository.onRightButtonUp()
                return true
            }
        }
        return super.onKeyUp(keyCode, event)
    }

    @Composable
    private fun UsualNavigator() {
        Navigator(
            screen = connectScreenProvider() as Screen,
            onBackPressed = { false },
        ) { navigator ->
            navigatorHolder.usualNavigator = navigator

            val state by joystickRepository.connect().collectAsState(InputDeviceData.empty())
            Text(
                modifier = Modifier.padding(top = 32.dp),
                text = state.toString(),
                color = Color.Black,
            )

            SlideTransition(navigator = navigator) { screen ->
                screen.Content()
            }
        }
    }
}