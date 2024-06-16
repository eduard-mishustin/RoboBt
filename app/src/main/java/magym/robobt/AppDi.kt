package magym.robobt

import magym.robobt.common.android.SingleActivityHolder
import magym.robobt.common.navigation.voyager.navigationModule
import magym.robobt.feature.connect.connectModule
import magym.robobt.feature.control.controlModule
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val roboModules: List<Module>
    get() = appModule + coreModules + commonModules + featureModules

private val appModule = module {
    singleOf(::SingleActivityHolder)
}

private val coreModules: List<Module>
    get() = listOf()

private val commonModules: List<Module>
    get() = listOf(
        navigationModule,
    )

private val featureModules: List<Module>
    get() = listOf(
        connectModule,
        controlModule,
    )