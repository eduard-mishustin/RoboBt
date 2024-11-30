package magym.robobt.feature.control

import magym.robobt.common.navigation.api.Router
import magym.robobt.feature.control.presentation.tea.ControlReducer
import magym.robobt.feature.control.presentation.tea.ControlStore
import magym.robobt.feature.control.presentation.tea.actor.ControlActor
import magym.robobt.feature.control.presentation.tea.actor.ControlNavigationActor
import magym.robobt.feature.control.presentation.tea.actor.ControlReadConnectionDataActor
import magym.robobt.feature.control.presentation.ui.ControlContentScreen
import magym.robobt.feature.control.presentation.ui.ControlUiStateMapper
import magym.robobt.repository.connect.bluetooth.BluetoothRepository
import magym.robobt.repository.input_device.accelerometer.AccelerometerRepository
import magym.robobt.repository.input_device.accelerometer.MotorSpeedMapper
import magym.robobt.repository.input_device.joystick.JoystickRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val controlFeatureModule = module {
    factory { ControlScreenProvider(::ControlContentScreen) }
    factoryOf(::createControlStore)
}

internal fun createControlStore(
    router: Router,
    bluetoothRepository: BluetoothRepository,
    accelerometerRepository: AccelerometerRepository,
    joystickRepository: JoystickRepository,
): ControlStore {
    return ControlStore(
        reducer = ControlReducer(),
        uiStateMapper = ControlUiStateMapper(),
        actors = setOf(
            ControlNavigationActor(router),
            ControlActor(
                bluetoothRepository = bluetoothRepository,
                accelerometerRepository = accelerometerRepository,
                joystickRepository = joystickRepository,
                motorSpeedMapper = MotorSpeedMapper(),
            ),
            ControlReadConnectionDataActor(bluetoothRepository),
        ),
    )
}