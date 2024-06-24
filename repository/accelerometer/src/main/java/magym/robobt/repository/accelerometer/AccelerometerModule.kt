package magym.robobt.repository.accelerometer

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorManager
import magym.robobt.repository.accelerometer.impl.AccelerometerRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val accelerometerModule = module {
    single<AccelerometerRepository> {
        val context = androidContext()

        val sensorManager = context.getSystemService(Activity.SENSOR_SERVICE) as SensorManager

        AccelerometerRepositoryImpl(
            sensorManager = sensorManager,
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        )
    }
}