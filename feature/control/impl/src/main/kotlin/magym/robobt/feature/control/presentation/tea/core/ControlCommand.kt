package magym.robobt.feature.control.presentation.tea.core

internal sealed interface ControlCommand {

    data object Control : ControlCommand
}

internal sealed interface ControlNavigationCommand : ControlCommand {

    data object OpenControl : ControlNavigationCommand
}