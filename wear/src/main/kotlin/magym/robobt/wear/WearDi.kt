package magym.robobt.wear

import magym.robobt.repository.accelerometer.accelerometerModule
import magym.robobt.repository.connect.bluetooth.bluetoothModule
import org.koin.core.module.Module

val wearModules: List<Module>
    get() = repositoryModules

private val repositoryModules: List<Module>
    get() = listOf(
        accelerometerModule,
        bluetoothModule,
    )