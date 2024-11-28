package magym.robobt.common.tea.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Composable
internal fun <T> rememberLifecycleFlow(
	flow: Flow<T>,
	lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
	lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
): Flow<T> = remember(flow, lifecycleOwner, lifecycleState) {
	flow.flowWithLifecycle(lifecycleOwner.lifecycle, lifecycleState)
}

@Composable
internal fun <T : R, R> Flow<T>.collectAsStateOnLifecycle(
	initial: R,
	context: CoroutineContext = EmptyCoroutineContext,
	lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
): State<R> {
	val lifecycleAwareFlow = rememberLifecycleFlow(this, lifecycleState)
	return lifecycleAwareFlow.collectAsState(initial, context)
}

@Suppress("StateFlowValueCalledInComposition")
@Composable
internal fun <T> StateFlow<T>.collectAsStateOnLifecycle(
	context: CoroutineContext = EmptyCoroutineContext,
	lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
): State<T> = collectAsStateOnLifecycle(value, context, lifecycleState)

@Composable
@Suppress("ComposableParametersOrdering", "ComposableNaming", "ComposableFunctionName")
internal fun <T> Flow<T>.collectOnLifecycle(
	lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
	collector: suspend CoroutineScope.(T) -> Unit,
) {
	val lifecycleFlow = rememberLifecycleFlow(this, lifecycleState)

	LaunchedEffect(lifecycleFlow) {
		withContext(Dispatchers.Main.immediate) {
			lifecycleFlow.collect { collector(it) }
		}
	}
}