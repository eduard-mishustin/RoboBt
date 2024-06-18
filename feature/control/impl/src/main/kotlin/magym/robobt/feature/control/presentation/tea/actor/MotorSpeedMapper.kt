@file:Suppress("SameParameterValue")

package magym.robobt.feature.control.presentation.tea.actor

import kotlin.math.absoluteValue
import kotlin.math.max
import magym.robobt.repository.accelerometer.model.AccelerometerData

internal class MotorSpeedMapper {

    fun map(data: AccelerometerData): Pair<Int, Int> {
        val (xAccel, yAccel) = data

        val x = -xAccel.applyThreshold().mapToMotorSpeedRange().toInt()
        val y = yAccel.applyThreshold().mapToMotorSpeedRange().toInt()

        val leftMotor: Int
        val rightMotor: Int

        when {
            // Right
            x > 0 -> {
                when {
                    // 1st quadrant
                    y >= 0 -> {
                        leftMotor = max(x.absoluteValue, y.absoluteValue)
                        rightMotor = y - x
                    }

                    // 4th quadrant
                    else -> {
                        leftMotor = y + x
                        rightMotor = (-max(x.absoluteValue, y.absoluteValue))
                    }
                }
            }

            // Left
            x < 0 -> {
                when {
                    // 2nd quadrant
                    y >= 0 -> {
                        leftMotor = y + x
                        rightMotor = max(x.absoluteValue, y.absoluteValue)
                    }

                    // 3rd quadrant
                    else -> {
                        leftMotor = (-max(x.absoluteValue, y.absoluteValue))
                        rightMotor = y - x
                    }
                }
            }

            // Straight
            else -> {
                leftMotor = y
                rightMotor = y
            }
        }

        return Pair(leftMotor, rightMotor)
    }

    private fun Float.applyThreshold(): Float {
        return when {
            this > ACCELEROMETER_TRESHOLD_MAX -> ACCELEROMETER_TRESHOLD_MAX
            this < -ACCELEROMETER_TRESHOLD_MAX -> -ACCELEROMETER_TRESHOLD_MAX
            this in -ACCELEROMETER_TRESHOLD_MIN..ACCELEROMETER_TRESHOLD_MIN -> 0f
            else -> this
        }
    }

    private fun Float.mapToMotorSpeedRange(): Float {
        return mapRange(
            value = this,
            from1 = -ACCELEROMETER_TRESHOLD_MAX,
            from2 = ACCELEROMETER_TRESHOLD_MAX,
            to1 = -255f,
            to2 = 255f,
        )
    }

    private fun mapRange(value: Float, from1: Float, from2: Float, to1: Float, to2: Float): Float {
        return ((value - from1) / (from2 - from1) * (to2 - to1) + to1)
    }

    private companion object {

        const val ACCELEROMETER_TRESHOLD_MIN = 2F // (0 - 9.8)
        const val ACCELEROMETER_TRESHOLD_MAX = 9F
    }
}
