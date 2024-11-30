package magym.robobt.feature.control.presentation.tea.core

internal sealed interface ControlCommand {

    sealed interface ControlMode : ControlCommand {

        data object Accelerometer : ControlMode

        data object Manual : ControlMode
    }

    sealed interface ReadConnectionData : ControlCommand {

        data object Subscribe : ReadConnectionData

        data object Unsubscribe : ReadConnectionData
    }

    sealed interface KeyboardAction : ControlCommand {

        data object OnTopLeftButtonDown : KeyboardAction

        data object OnTopLeftButtonUp : KeyboardAction

        data object OnTopRightButtonDown : KeyboardAction

        data object OnTopRightButtonUp : KeyboardAction

        data object OnBottomLeftButtonDown : KeyboardAction

        data object OnBottomLeftButtonUp : KeyboardAction

        data object OnBottomRightButtonDown : KeyboardAction

        data object OnBottomRightButtonUp : KeyboardAction
    }
}

internal sealed interface ControlNavigationCommand : ControlCommand {

    data object Exit : ControlNavigationCommand
}