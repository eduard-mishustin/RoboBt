package magym.robobt.repository.input_device.accelerometer.impl.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest

fun <T> Flow<T>.customDebounce(
	debounce: Long = 300L,
): Flow<T> {
	fun now() = System.currentTimeMillis()

	var bufferedEndWindowTimestamp = 0L

	return transformLatest { value ->
		if (bufferedEndWindowTimestamp == 0L) {
			bufferedEndWindowTimestamp = now() + debounce
			delay(debounce)
		} else {
			val remainedLoadingDuration = bufferedEndWindowTimestamp - now()
			if (remainedLoadingDuration > 0L) {
				delay(remainedLoadingDuration)
			}
			bufferedEndWindowTimestamp = 0L
		}

		emit(value)
	}
}
