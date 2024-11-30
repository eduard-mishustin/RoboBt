package magym.robobt.repository.input_device.accelerometer.model

data class ControlMotorsData(
    val leftMotor: Int,
    val rightMotor: Int,
) {

    companion object {

        fun empty(): ControlMotorsData {
            return ControlMotorsData(
                leftMotor = 0,
                rightMotor = 0
            )
        }
    }
}