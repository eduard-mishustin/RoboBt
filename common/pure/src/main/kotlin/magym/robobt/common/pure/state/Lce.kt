package magym.robobt.common.pure.state

sealed interface Lce<out T> {

	data class Loading<T>(val content: T? = null) : Lce<T>
	data class Content<T>(val content: T) : Lce<T>
	data class Error(val error: Throwable? = null) : Lce<Nothing>

	companion object {
		fun initial(): Lce<Nothing> = Loading()
	}
}