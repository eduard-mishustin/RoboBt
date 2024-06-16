package magym.robobt.common.navigation.api

enum class ExternalDestination {
	CAMERA,
	GALLERY
}

sealed class ExternalResult<T> {

	data class Success<T>(val result: T) : ExternalResult<T>()

	data object Cancel : ExternalResult<Nothing>()
}

interface ExternalRouter {

	suspend fun <T> navigateForResult(destination: ExternalDestination): ExternalResult<T>
}