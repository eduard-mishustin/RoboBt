package magym.robobt.common.navigation.voyager.impl.result

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

internal class NavigationResultEventBus {

	private val results = MutableSharedFlow<Pair<String, Any?>>()

	fun sendResult(key: String, result: Any?) = runBlocking {
		results.emit(key to result)
	}

	@Suppress("UNCHECKED_CAST")
	suspend fun <R> awaitResult(key: String): R {
		return results
			.first { (resultKey, _) -> resultKey == key }
			.second as R
	}
}