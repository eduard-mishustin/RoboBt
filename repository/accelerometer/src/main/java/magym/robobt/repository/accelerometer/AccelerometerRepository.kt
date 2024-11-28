package magym.robobt.repository.accelerometer

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import magym.robobt.repository.accelerometer.model.AccelerometerData
import magym.robobt.repository.accelerometer.model.ControlMotorsData

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

// сделать контроллеры репозитории, возвращать ControlMotorsData. motorSpeedMapper вызывать внутри них. AccelerometerRepository будет использоваться в ControllerAccelerometerRepository

val joystickFlow = MutableStateFlow(AccelerometerData(0f, 0f))
val joystickTriggersFlow = MutableStateFlow(ControlMotorsData(0, 0))
