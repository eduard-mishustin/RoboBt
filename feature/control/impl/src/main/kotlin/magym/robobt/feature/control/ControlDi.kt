package magym.robobt.feature.control

import magym.robobt.feature.control.presentation.tea.ControlReducer
import magym.robobt.feature.control.presentation.tea.ControlStore
import magym.robobt.feature.control.presentation.tea.actor.ControlActor
import magym.robobt.feature.control.presentation.ui.ControlContentScreen
import magym.robobt.feature.control.presentation.ui.ControlUiStateMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val controlModule = module {
    factory { ControlScreenProvider(::ControlContentScreen) }
    factoryOf(::createControlStore)
}

internal fun createControlStore(): ControlStore {
    return ControlStore(
        reducer = ControlReducer(),
        uiStateMapper = ControlUiStateMapper(),
        actors = setOf(
            ControlActor(),
        ),
    )
}