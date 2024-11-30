package magym.robobt.repository.input_device

import kotlinx.coroutines.flow.Flow

/**
 * Return flow of [InputDeviceData] with values of x, y.
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
interface InputDeviceRepository {

    fun connect(): Flow<InputDeviceData>
}