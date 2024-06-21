package magym.robobt.repository.accelerometer

import android.hardware.Sensor
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import magym.robobt.repository.accelerometer.model.AccelerometerData
import magym.robobt.repository.accelerometer.util.onAccelerometerChanged

/**
 * Return flow of [AccelerometerData] with values of x, y, z.
 *
 * Values range: [-10.0, 10.0]
 *
 * Quadrants:
 * 1st: x > 0, y > 0
 * 2nd: x > 0, y < 0
 * 3rd: x < 0, y < 0
 * 4th: x < 0, y > 0
 *
 * ┌───────────────────┐
 * │    4    Y    1    │
 * │         │         │
 * │─────────│────────X│
 * │    3    │    2    │
 * │         │         │
 * └───────────────────┘
 */
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
                x = -event.values[0].roundTo1DecimalPlaces(),
                y = -event.values[1].roundTo1DecimalPlaces(),
                z = event.values[2].roundTo1DecimalPlaces(),
            )

            trySend(data)
        }

        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        awaitClose { sensorManager.unregisterListener(listener) }
    }.distinctUntilChanged()

    private fun Float.roundTo1DecimalPlaces(): Float {
        return (this * 10).toInt() / 10f
    }
}