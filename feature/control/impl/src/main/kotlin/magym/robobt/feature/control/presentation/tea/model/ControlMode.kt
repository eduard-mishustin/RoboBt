package magym.robobt.feature.control.presentation.tea.model

internal enum class ControlMode {

    Manual,
    Accelerometer;

    fun next(): ControlMode {
        return when (this) {
            Manual -> Accelerometer
            Accelerometer -> Manual
        }
    }
}