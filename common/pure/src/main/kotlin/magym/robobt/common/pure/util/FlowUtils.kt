package magym.robobt.common.pure.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

fun <T> flowBy(block: suspend () -> T): Flow<T> = block.asFlow()

fun <T> Flow<T>.startWith(value: T): Flow<T> = onStart { emit(value) }

fun <T> Flow<T>.onCatchReturn(block: (Throwable) -> T) = catch { error -> emit(block(error)) }

fun <T> Flow<T>.ignoreResult() = map { null }.filterNotNull()