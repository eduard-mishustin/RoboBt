package magym.robobt.feature.control.presentation.tea.core

import magym.robobt.feature.control.presentation.tea.model.ControlMotorsData

internal sealed interface ControlCommand {

    sealed interface ControlMode : ControlCommand {

        data object Accelerometer : ControlMode

        data class Manual(val motorsData: ControlMotorsData) : ControlMode
    }
}

internal sealed interface ControlNavigationCommand : ControlCommand {

    data object Exit : ControlNavigationCommand
}