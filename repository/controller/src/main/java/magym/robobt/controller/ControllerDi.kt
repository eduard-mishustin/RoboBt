package magym.robobt.controller

import magym.robobt.controller.accelerometer.ControllerAccelerometerRepository
import magym.robobt.controller.accelerometer.ControllerAccelerometerRepositoryImpl
import magym.robobt.controller.joystick.ControllerJoystickRepository
import magym.robobt.controller.joystick.ControllerJoystickRepositoryImpl
import magym.robobt.controller.joystick_triggers.ControllerJoystickTriggersRepository
import magym.robobt.controller.joystick_triggers.ControllerJoystickTriggersRepositoryImpl
import magym.robobt.controller.joystick_triggers.MutableControllerJoystickTriggersRepository
import magym.robobt.controller.keyboard.ControllerKeyboardRepository
import magym.robobt.controller.keyboard.ControllerKeyboardRepositoryImpl
import magym.robobt.controller.keyboard.MutableControllerKeyboardRepository
import magym.robobt.controller.mapper.MotorSpeedMapper
import magym.robobt.controller.web.ControllerWebRepository
import magym.robobt.controller.web.ControllerWebRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val controllerRepositoryModule = module {
    single { MotorSpeedMapper() }
    singleOf(::ControllerAccelerometerRepositoryImpl) bind ControllerAccelerometerRepository::class
    singleOf(::ControllerJoystickRepositoryImpl) bind ControllerJoystickRepository::class
    singleOf(::ControllerWebRepositoryImpl) bind ControllerWebRepository::class

    singleOf(::ControllerJoystickTriggersRepositoryImpl)
    single<ControllerJoystickTriggersRepository> { get<ControllerJoystickTriggersRepositoryImpl>() }
    single<MutableControllerJoystickTriggersRepository> { get<ControllerJoystickTriggersRepositoryImpl>() }

    singleOf(::ControllerKeyboardRepositoryImpl)
    single<ControllerKeyboardRepository> { get<ControllerKeyboardRepositoryImpl>() }
    single<MutableControllerKeyboardRepository> { get<ControllerKeyboardRepositoryImpl>() }
}