package magym.robobt.common.tea

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface Store<Effect : Any, UiEvent : Any, UiState : Any> {

	val effects: Flow<Effect>

	val state: StateFlow<UiState>

	fun accept(event: UiEvent)
}