package magym.robobt.common.tea.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun <UiEvent : Any> TeaScope<UiEvent, *>.acceptable(event: UiEvent): () -> Unit {
	return rememberAcceptable { accept(event) }
}

@Composable
fun <UiEvent : Any, T1> TeaScope<UiEvent, *>.acceptable(block: (T1) -> UiEvent): (T1) -> Unit {
	return rememberAcceptable { t1 -> accept(block(t1)) }
}

@Composable
fun <UiEvent : Any, T1, T2> TeaScope<UiEvent, *>.acceptable(block: (T1, T2) -> UiEvent): (T1, T2) -> Unit {
	return rememberAcceptable { t1, t2 -> accept(block(t1, t2)) }
}

@Composable
fun <UiEvent : Any, T1, T2, T3> TeaScope<UiEvent, *>.acceptable(block: (T1, T2, T3) -> UiEvent): (T1, T2, T3) -> Unit {
	return rememberAcceptable { t1, t2, t3 -> accept(block(t1, t2, t3)) }
}

@Composable
fun <UiEvent : Any, T1, T2, T3, T4> TeaScope<UiEvent, *>.acceptable(block: (T1, T2, T3, T4) -> UiEvent): (T1, T2, T3, T4) -> Unit {
	return rememberAcceptable { t1, t2, t3, t4 -> accept(block(t1, t2, t3, t4)) }
}

@Composable
fun <UiEvent : Any, T1, T2, T3, T4, T5> TeaScope<UiEvent, *>.acceptable(block: (T1, T2, T3, T4, T5) -> UiEvent): (T1, T2, T3, T4, T5) -> Unit {
	return rememberAcceptable { t1, t2, t3, t4, t5 -> accept(block(t1, t2, t3, t4, t5)) }
}

@Composable
fun <UiEvent : Any, T1, T2, T3, T4, T5, T6> TeaScope<UiEvent, *>.acceptable(block: (T1, T2, T3, T4, T5, T6) -> UiEvent): (T1, T2, T3, T4, T5, T6) -> Unit {
	return rememberAcceptable { t1, t2, t3, t4, t5, t6 -> accept(block(t1, t2, t3, t4, t5, t6)) }
}

@Composable
fun <UiEvent : Any, T1, T2, T3, T4, T5, T6, T7> TeaScope<UiEvent, *>.acceptable(block: (T1, T2, T3, T4, T5, T6, T7) -> UiEvent): (T1, T2, T3, T4, T5, T6, T7) -> Unit {
	return rememberAcceptable { t1, t2, t3, t4, t5, t6, t7 -> accept(block(t1, t2, t3, t4, t5, t6, t7)) }
}

@Composable
fun <UiEvent : Any, T1, T2, T3, T4, T5, T6, T7, T8> TeaScope<UiEvent, *>.acceptable(block: (T1, T2, T3, T4, T5, T6, T7, T8) -> UiEvent): (T1, T2, T3, T4, T5, T6, T7, T8) -> Unit {
	return rememberAcceptable { t1, t2, t3, t4, t5, t6, t7, t8 -> accept(block(t1, t2, t3, t4, t5, t6, t7, t8)) }
}

@Composable
fun <UiEvent : Any, T1, T2, T3, T4, T5, T6, T7, T8, T9> TeaScope<UiEvent, *>.acceptable(block: (T1, T2, T3, T4, T5, T6, T7, T8, T9) -> UiEvent): (T1, T2, T3, T4, T5, T6, T7, T8, T9) -> Unit {
	return rememberAcceptable { t1, t2, t3, t4, t5, t6, t7, t8, t9 -> accept(block(t1, t2, t3, t4, t5, t6, t7, t8, t9)) }
}

@Composable
private fun <T> TeaScope<*, *>.rememberAcceptable(acceptable: T): T {
	return remember(this) { acceptable }
}
