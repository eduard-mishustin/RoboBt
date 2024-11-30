package magym.robobt.feature.control.presentation.tea.actor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import magym.robobt.common.tea.component.Actor
import magym.robobt.controller.keyboard.MutableControllerKeyboardRepository
import magym.robobt.feature.control.presentation.tea.core.ControlCommand
import magym.robobt.feature.control.presentation.tea.core.ControlCommand.KeyboardAction
import magym.robobt.feature.control.presentation.tea.core.ControlEvent
import magym.robobt.feature.control.presentation.tea.core.ControlEvent.Controlling

internal class KeyboardActor(
    private val controllerKeyboardRepository: MutableControllerKeyboardRepository
) : Actor<ControlCommand, ControlEvent> {

    override fun act(commands: Flow<ControlCommand>): Flow<ControlEvent> {
        return commands.filterIsInstance<KeyboardAction>()
            .mapLatest(::handleCommand)
            .filterNotNull()
    }

    private suspend fun handleCommand(command: KeyboardAction): Controlling? {
        when (command) {
            KeyboardAction.OnTopLeftButtonDown -> controllerKeyboardRepository.onTopLeftButtonDown()
            KeyboardAction.OnTopLeftButtonUp -> controllerKeyboardRepository.onTopLeftButtonUp()
            KeyboardAction.OnTopRightButtonDown -> controllerKeyboardRepository.onTopRightButtonDown()
            KeyboardAction.OnTopRightButtonUp -> controllerKeyboardRepository.onTopRightButtonUp()
            KeyboardAction.OnBottomLeftButtonDown -> controllerKeyboardRepository.onBottomLeftButtonDown()
            KeyboardAction.OnBottomLeftButtonUp -> controllerKeyboardRepository.onBottomLeftButtonUp()
            KeyboardAction.OnBottomRightButtonDown -> controllerKeyboardRepository.onBottomRightButtonDown()
            KeyboardAction.OnBottomRightButtonUp -> controllerKeyboardRepository.onBottomRightButtonUp()
        }

        return null
    }
}