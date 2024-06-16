package magym.robobt.common.navigation.voyager.impl

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import magym.robobt.common.navigation.api.RoboScreen
import magym.robobt.common.navigation.api.DestinationMode
import magym.robobt.common.navigation.api.NoScreen
import magym.robobt.common.navigation.api.Router
import magym.robobt.common.navigation.voyager.impl.result.NavigationResultEventBus

internal class RouterImpl(
    private val navigatorHolder: NavigatorHolder,
    private val resultEventBus: NavigationResultEventBus,
    private val applicationExitProvider: ApplicationExitProvider,
) : Router {

    private val navigator: Navigator
        get() = navigatorHolder.usualNavigator ?: error("Navigator is not initialized")

    private val bottomSheetNavigator: BottomSheetNavigator
        get() = navigatorHolder.bottomSheetNavigator ?: error("bottomSheetNavigator is not initialized")

    private val overlayNavigator: Navigator
        get() = navigatorHolder.overlayNavigator ?: error("overlayNavigator is not initialized")

    private val isBottomSheetVisible: Boolean
        get() = bottomSheetNavigator.isVisible

    private val isOverlayVisible: Boolean
        get() = overlayNavigator.lastItemOrNull !is NoScreen

    private val screenKey: ScreenKey
        get() = when {
            isBottomSheetVisible -> bottomSheetNavigator.lastItemOrNull?.key ?: error("bottomSheetNavigator screen is not provided")
            isOverlayVisible -> overlayNavigator.lastItem.key
            else -> navigator.lastItem.key
        }

    override fun navigate(screen: RoboScreen, mode: DestinationMode) {
        when (mode) {
            DestinationMode.USUAL -> navigator.push(screen as Screen)
            DestinationMode.BOTTOM_SHEET -> bottomSheetNavigator.show(screen as Screen)
            DestinationMode.OVERLAY -> overlayNavigator.push(screen as Screen)
        }
    }

    override fun replace(screen: RoboScreen, mode: DestinationMode) {
        when (mode) {
            DestinationMode.USUAL -> navigator.replace(screen as Screen)
            DestinationMode.BOTTOM_SHEET -> bottomSheetNavigator.replace(screen as Screen)
            DestinationMode.OVERLAY -> overlayNavigator.replace(screen as Screen)
        }
    }

    override suspend fun <T> navigateForResult(screen: RoboScreen, mode: DestinationMode): T {
        navigate(screen, mode)
        return resultEventBus.awaitResult((screen as Screen).key)
    }

    override fun exit() {
        when {
            isOverlayVisible -> overlayNavigator.pop()
            isBottomSheetVisible -> bottomSheetNavigator.hide() // TODO: Think about pop here
            else -> if (!navigator.pop()) {
                applicationExitProvider.exit()
            }
        }
    }

    override fun exitWithResult(result: Any?) {
        resultEventBus.sendResult(screenKey, result)
        exit()
    }
}