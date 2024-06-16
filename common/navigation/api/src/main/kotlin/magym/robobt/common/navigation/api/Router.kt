package magym.robobt.common.navigation.api

interface Router {

	fun navigate(
        screen: RoboScreen,
        mode: DestinationMode = DestinationMode.USUAL,
	)

	fun replace(
        screen: RoboScreen,
        mode: DestinationMode = DestinationMode.USUAL,
	)

	suspend fun <T> navigateForResult(
        screen: RoboScreen,
        mode: DestinationMode = DestinationMode.USUAL,
	): T

	fun exit()

	fun exitWithResult(result: Any?)
}