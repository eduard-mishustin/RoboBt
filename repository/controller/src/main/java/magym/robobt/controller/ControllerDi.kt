package magym.robobt.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
import magym.robobt.controller.remote.ControllerRemoteRepository
import magym.robobt.controller.remote.ControllerRemoteRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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

    singleOf(::ControllerKeyboardRepositoryImpl)
    single<ControllerKeyboardRepository> { get<ControllerKeyboardRepositoryImpl>() }
    single<MutableControllerKeyboardRepository> { get<ControllerKeyboardRepositoryImpl>() }

    single<ControllerRemoteRepository> {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        ControllerRemoteRepositoryImpl(
            okHttpClient = okHttpClient,
            scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        )
    }
}