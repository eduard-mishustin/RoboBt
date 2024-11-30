@file:Suppress("UNUSED_PARAMETER")

package magym.robobt

import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import magym.robobt.controller.joystick_triggers.MutableControllerJoystickTriggersRepository
import magym.robobt.repository.input_device.joystick.MutableJoystickRepository

class ActivityGenericMotionDelegate(
    private val joystickRepository: MutableJoystickRepository,
    private val joystickTriggersRepository: MutableControllerJoystickTriggersRepository,
) {

    fun onGenericMotionEvent(event: MotionEvent): Boolean {
        val device = event.device

        if (device != null && device.sources and InputDevice.SOURCE_JOYSTICK != 0) {
            val xLeftStick = event.getAxisValue(MotionEvent.AXIS_X, 0)
            val yLeftStick = event.getAxisValue(MotionEvent.AXIS_Y, 0)
            val leftTrigger = event.getAxisValue(MotionEvent.AXIS_LTRIGGER)
            val rightTrigger = event.getAxisValue(MotionEvent.AXIS_RTRIGGER)

            joystickRepository.onStickInputChanged(xLeftStick, yLeftStick)
            joystickTriggersRepository.onTriggerInputChanged(leftTrigger, rightTrigger)
            return true
        }

        return false
    }

    fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
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

        return false
    }

    fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
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

        return false
    }
}