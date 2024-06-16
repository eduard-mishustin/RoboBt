package magym.robobt.common.tea.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import magym.robobt.common.tea.Store
import kotlinx.coroutines.CoroutineScope

@Stable
interface TeaScope<in UiEvent : Any, out Effect : Any> {

	fun accept(event: UiEvent)

	@Composable
	fun EffectHandler(renderer: suspend CoroutineScope.(Effect) -> Unit)
}

@Composable
internal fun <UiEvent : Any, Effect : Any> rememberTeaScope(
    store: Store<Effect, UiEvent, *>,
    lifecycleState: Lifecycle.State,
): TeaScope<UiEvent, Effect> {
	return remember(store, lifecycleState) {
		TeaScopeImpl(store, lifecycleState)
	}
}

private class TeaScopeImpl<in UiEvent : Any, out Effect : Any>(
    private val store: Store<Effect, UiEvent, *>,
    private val lifecycleState: Lifecycle.State,
) : TeaScope<UiEvent, Effect> {

	override fun accept(event: UiEvent) {
		store.accept(event)
	}

	@Composable
	override fun EffectHandler(renderer: suspend CoroutineScope.(Effect) -> Unit) {
		store.effects.collectOnLifecycle(lifecycleState, renderer)
	}
}