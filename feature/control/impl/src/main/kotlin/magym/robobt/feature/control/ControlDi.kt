package magym.robobt.feature.control

import magym.robobt.common.navigation.api.Router
import magym.robobt.controller.accelerometer.ControllerAccelerometerRepository
import magym.robobt.controller.joystick.ControllerJoystickRepository
import magym.robobt.controller.joystick_triggers.ControllerJoystickTriggersRepository
import magym.robobt.controller.keyboard.MutableControllerKeyboardRepository
import magym.robobt.controller.remote.MutableControllerRemoteRepository
import magym.robobt.feature.control.presentation.tea.ControlReducer
import magym.robobt.feature.control.presentation.tea.ControlStore
import magym.robobt.feature.control.presentation.tea.actor.ControlActor
import magym.robobt.feature.control.presentation.tea.actor.ControlNavigationActor
import magym.robobt.feature.control.presentation.tea.actor.ControlReadConnectionDataActor
import magym.robobt.feature.control.presentation.tea.actor.KeyboardActor
import magym.robobt.feature.control.presentation.ui.ControlContentScreen
import magym.robobt.feature.control.presentation.ui.ControlUiStateMapper
import magym.robobt.repository.connect.bluetooth.BluetoothRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val controlFeatureModule = module {
    factory { ControlScreenProvider(::ControlContentScreen) }
    factoryOf(::createControlStore)
}

internal fun createControlStore(
    router: Router,
    bluetoothRepository: BluetoothRepository,
    controllerAccelerometerRepository: ControllerAccelerometerRepository,
    controllerJoystickRepository: ControllerJoystickRepository,
    controllerJoystickTriggersRepository: ControllerJoystickTriggersRepository,
    controllerKeyboardRepository: MutableControllerKeyboardRepository,
    controllerRemoteRepository: MutableControllerRemoteRepository,
): ControlStore {
    return ControlStore(
        reducer = ControlReducer(),
        uiStateMapper = ControlUiStateMapper(),
        actors = setOf(
            ControlNavigationActor(router),
            ControlActor(
                bluetoothRepository = bluetoothRepository,
                controllerRemoteRepository = controllerRemoteRepository,

                keyboardRepository = controllerKeyboardRepository,
                accelerometerRepository = controllerAccelerometerRepository,
                joystickRepository = controllerJoystickRepository,
                joystickTriggersRepository = controllerJoystickTriggersRepository,
            ),
            ControlReadConnectionDataActor(bluetoothRepository),
            KeyboardActor(controllerKeyboardRepository)
        ),
    )
}