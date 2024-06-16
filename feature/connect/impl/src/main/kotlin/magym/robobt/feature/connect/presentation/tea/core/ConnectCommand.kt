package magym.robobt.feature.connect.presentation.tea.core

internal sealed interface ConnectCommand {

    data object Connect : ConnectCommand
}

internal sealed interface ConnectNavigationCommand : ConnectCommand {

    data object OpenControl : ConnectNavigationCommand
}