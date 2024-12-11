package magym.robobt.repository.input_device

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorManager
import magym.robobt.repository.input_device.accelerometer.AccelerometerRepository
import magym.robobt.repository.input_device.accelerometer.AccelerometerRepositoryImpl
import magym.robobt.repository.input_device.joystick.JoystickRepository
import magym.robobt.repository.input_device.joystick.JoystickRepositoryImpl
import magym.robobt.repository.input_device.joystick.MutableJoystickRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val accelerometerRepositoryModule = module {
    single<AccelerometerRepository> {
        val context = androidContext()
        val sensorManager = context.getSystemService(Activity.SENSOR_SERVICE) as SensorManager

        AccelerometerRepositoryImpl(
            sensorManager = sensorManager,
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        )
    }

    singleOf(::JoystickRepositoryImpl)
    single<JoystickRepository> { get<JoystickRepositoryImpl>() }
    single<MutableJoystickRepository> { get<JoystickRepositoryImpl>() }
}