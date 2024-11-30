package magym.robobt.repository.input_device.accelerometer.util

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

internal fun onAccelerometerChanged(callback: (SensorEvent) -> Unit): SensorEventListener {
    return object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) = Unit

        override fun onSensorChanged(event: SensorEvent) {
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    callback.invoke(event)
                }
            }
        }
    }
}