package magym.robobt.feature.control.presentation.tea.core

import magym.robobt.common.android.orientation.Orientation
import magym.robobt.repository.accelerometer.model.ControlMotorsData

internal sealed interface ControlCommand {

    sealed interface ControlMode : ControlCommand {

        data object Accelerometer : ControlMode

        data class Manual(val motorsData: ControlMotorsData) : ControlMode
    }

    sealed interface ReadConnectionData : ControlCommand {

        data object Subscribe : ReadConnectionData

        data object Unsubscribe : ReadConnectionData
    }

    data class ChangeOrientation(val orientation: Orientation) : ControlCommand
}

internal sealed interface ControlNavigationCommand : ControlCommand {

    data object Exit : ControlNavigationCommand
}