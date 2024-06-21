package magym.robobt.feature.control.presentation.tea.core

import magym.robobt.feature.control.presentation.tea.model.ControlOrientation

internal sealed interface ControlEffect {

    data class ChangeControlOrientation(val mode: ControlOrientation) : ControlEffect
}