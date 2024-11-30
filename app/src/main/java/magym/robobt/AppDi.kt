package magym.robobt

import magym.robobt.common.android.SingleActivityHolder
import magym.robobt.common.navigation.voyager.navigationModule
import magym.robobt.feature.connect.connectFeatureModule
import magym.robobt.feature.control.controlFeatureModule
import magym.robobt.repository.connect.bluetooth.bluetoothRepositoryModule
import magym.robobt.repository.input_device.accelerometerRepositoryModule
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val roboModules: List<Module>
    get() = appModule + coreModules + commonModules + featureModules + repositoryModules

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
        connectFeatureModule,
        controlFeatureModule,
    )

private val repositoryModules: List<Module>
    get() = listOf(
        accelerometerRepositoryModule,
        bluetoothRepositoryModule,
    )