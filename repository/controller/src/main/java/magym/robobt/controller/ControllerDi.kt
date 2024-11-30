package magym.robobt.controller

import magym.robobt.controller.accelerometer.ControllerAccelerometerRepository
import magym.robobt.controller.accelerometer.ControllerAccelerometerRepositoryImpl
import magym.robobt.controller.mapper.MotorSpeedMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val controllerRepositoryModule = module {
    single { MotorSpeedMapper() }
    singleOf(::ControllerAccelerometerRepositoryImpl) bind ControllerAccelerometerRepository::class
}