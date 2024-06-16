package magym.robobt.feature.connect

import magym.robobt.common.navigation.api.Router
import magym.robobt.feature.connect.presentation.tea.ConnectReducer
import magym.robobt.feature.connect.presentation.tea.ConnectStore
import magym.robobt.feature.connect.presentation.tea.actor.ConnectActor
import magym.robobt.feature.connect.presentation.ui.ConnectContentScreen
import magym.robobt.feature.connect.presentation.ui.ConnectUiStateMapper
import magym.robobt.feature.control.ControlScreenProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val connectModule = module {
    factory { ConnectScreenProvider(::ConnectContentScreen) }
    factoryOf(::createConnectStore)
}

internal fun createConnectStore(
    router: Router,
    controlScreenProvider: ControlScreenProvider,
): ConnectStore {
    return ConnectStore(
        reducer = ConnectReducer(),
        uiStateMapper = ConnectUiStateMapper(),
        actors = setOf(
            //MainNavigationActor(router, controlScreenProvider),
            ConnectActor(),
        ),
    )
}