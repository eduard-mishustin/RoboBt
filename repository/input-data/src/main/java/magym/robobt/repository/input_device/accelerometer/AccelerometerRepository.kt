package magym.robobt.repository.input_device.accelerometer

import android.hardware.Sensor
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import magym.robobt.repository.input_device.InputDeviceData
import magym.robobt.repository.input_device.InputDeviceRepository
import magym.robobt.repository.input_device.accelerometer.util.onAccelerometerChanged

interface AccelerometerRepository : InputDeviceRepository

internal class AccelerometerRepositoryImpl(
    private val sensorManager: SensorManager,
    private val sensor: Sensor?,
) : AccelerometerRepository {

    override fun connect(): Flow<InputDeviceData> = callbackFlow {
        val listener = onAccelerometerChanged { event ->
            val data = InputDeviceData(
                x = -event.values[0].roundTo1DecimalPlaces(),
                y = -event.values[1].roundTo1DecimalPlaces(),
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