package magym.robobt.repository.input_device

data class InputDeviceData(
    val x: Float,
    val y: Float,
) {

    companion object {

        fun empty(): InputDeviceData {
            return InputDeviceData(
                x = 0f,
                y = 0f
            )
        }
    }
}