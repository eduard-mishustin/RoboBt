package magym.robobt.controller

data class ControlMotorsData(
    val leftMotor: Int,
    val rightMotor: Int,
) {

    val isNotEmpty: Boolean
        get() = leftMotor != 0 || rightMotor != 0

    companion object {

        fun empty(): ControlMotorsData {
            return ControlMotorsData(
                leftMotor = 0,
                rightMotor = 0
            )
        }
    }
}