package magym.robobt.repository.accelerometer

import android.hardware.Sensor
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import magym.robobt.repository.accelerometer.model.AccelerometerData
import magym.robobt.repository.accelerometer.util.onAccelerometerChanged

interface AccelerometerRepository {

    fun connect(): Flow<AccelerometerData>
}

internal class AccelerometerRepositoryImpl(
    private val sensorManager: SensorManager,
    private val sensor: Sensor?,
) : AccelerometerRepository {

    override fun connect(): Flow<AccelerometerData> = callbackFlow {
        val listener = onAccelerometerChanged { event ->
            val data = AccelerometerData(
                x = event.values[0],
                y = event.values[1],
                z = event.values[2],
            )

            trySend(data)
        }

        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        awaitClose { sensorManager.unregisterListener(listener) }
    }
}