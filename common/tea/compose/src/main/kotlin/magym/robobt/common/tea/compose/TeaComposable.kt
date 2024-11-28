package magym.robobt.common.tea.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.Dispatchers
import magym.robobt.common.tea.Store

@Composable
fun <UiState : Any, UiEvent : Any, Effect : Any> TeaComposable(
    store: Store<Effect, UiEvent, UiState>,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    content: @Composable TeaScope<UiEvent, Effect>.(state: UiState) -> Unit,
) {
	val scope = rememberTeaScope(lifecycleState = lifecycleState, store = store)
	val state by store.state.collectAsStateOnLifecycle(Dispatchers.Main.immediate, lifecycleState)

	content(scope, state)
}