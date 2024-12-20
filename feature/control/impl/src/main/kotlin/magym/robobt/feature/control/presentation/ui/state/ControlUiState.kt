package magym.robobt.feature.control.presentation.ui.state

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface ControlUiState {

    val header: Header

    data class Manual(override val header: Header) : ControlUiState

    data class Accelerometer(override val header: Header) : ControlUiState

    data class Header(
        val leftMotor: Int,
        val rightMotor: Int,
        val weather: String?,
    )

    companion object {

        fun manualPreview() = Manual(
            header = Header(
                leftMotor = 255,
                rightMotor = 255,
                weather = "27 °C, 30%",
            )
        )

        fun accelerometerPreview() = Accelerometer(
            header = Header(
                leftMotor = 0,
                rightMotor = 0,
                weather = null,
            )
        )
    }
}