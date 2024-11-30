package magym.robobt.repository.input_device.joystick

interface MutableJoystickRepository : JoystickRepository {

    fun onStickInputChanged(x: Float, y: Float)
}