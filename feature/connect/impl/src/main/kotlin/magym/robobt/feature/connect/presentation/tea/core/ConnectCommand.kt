package magym.robobt.feature.connect.presentation.tea.core

import magym.robobt.common.android.orientation.Orientation

internal sealed interface ConnectCommand {

    data object Connect : ConnectCommand

    data class ChangeOrientation(val orientation: Orientation) : ConnectCommand
}

internal sealed interface ConnectNavigationCommand : ConnectCommand {

    data object OpenControl : ConnectNavigationCommand
}