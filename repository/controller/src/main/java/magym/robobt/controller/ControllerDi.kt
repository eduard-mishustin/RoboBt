package magym.robobt.controller

import magym.robobt.controller.accelerometer.ControllerAccelerometerRepository
import magym.robobt.controller.accelerometer.ControllerAccelerometerRepositoryImpl
import magym.robobt.controller.joystick.ControllerJoystickRepository
import magym.robobt.controller.joystick.ControllerJoystickRepositoryImpl
import magym.robobt.controller.joystick_triggers.ControllerJoystickTriggersRepository
import magym.robobt.controller.joystick_triggers.ControllerJoystickTriggersRepositoryImpl
import magym.robobt.controller.joystick_triggers.MutableControllerJoystickTriggersRepository
import magym.robobt.controller.mapper.MotorSpeedMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val controllerRepositoryModule = module {
    single { MotorSpeedMapper() }
    singleOf(::ControllerAccelerometerRepositoryImpl) bind ControllerAccelerometerRepository::class
    singleOf(::ControllerJoystickRepositoryImpl) bind ControllerJoystickRepository::class

    singleOf(::ControllerJoystickTriggersRepositoryImpl)
    single<ControllerJoystickTriggersRepository> { get<ControllerJoystickTriggersRepositoryImpl>() }
    single<MutableControllerJoystickTriggersRepository> { get<ControllerJoystickTriggersRepositoryImpl>() }
}