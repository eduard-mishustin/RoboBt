package magym.robobt.common.pure.state

import magym.robobt.common.pure.state.Lce.Content
import magym.robobt.common.pure.state.Lce.Error
import magym.robobt.common.pure.state.Lce.Loading

val <T> Lce<T>.content: T?
	get() {
		return when (this) {
			is Content -> content
			is Loading -> content
			is Error -> null
		}
	}

fun <T> Lce<T>.requireContent(): T = requireNotNull(content)

val <T> Lce<T>.isError: Boolean
	get() = this is Error

val <T> Lce<T>.isLoading: Boolean
	get() = this is Loading

val <T> Lce<T>.isContent: Boolean
	get() = this is Content

fun <T> Lce<T>.toLoadingContentAware(): Lce<T> {
	return if (this is Content) this
	else Loading()
}

fun <T : Any> Lce<T>.toLoading(): Lce<T> {
	return Loading(content)
}