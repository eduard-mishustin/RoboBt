@file:Suppress("SameParameterValue")

package magym.robobt.repository.input_device.accelerometer

import magym.robobt.repository.input_device.InputDeviceData
import magym.robobt.repository.input_device.accelerometer.model.ControlMotorsData
import kotlin.math.absoluteValue
import kotlin.math.max

class MotorSpeedMapper(
    private val accelerometerTresholdMin: Float = 2F, // (0 - 10)
    private val accelerometerTresholdMax: Float = 7F,
) {

    fun map(data: InputDeviceData): ControlMotorsData {
        val (xAccel, yAccel) = data

        val x = xAccel.applyThreshold().mapToMotorSpeedRange().toInt()
        val y = yAccel.applyThreshold().mapToMotorSpeedRange().toInt()

        val (leftMotor, rightMotor) = calculateMotorSpeeds(x, y)

        return ControlMotorsData(leftMotor, rightMotor).also { result ->
            println("MotorSpeedMapper: data = $data; x = $x, y = $y; result = $result")
        }
    }

    private fun Float.applyThreshold(): Float {
        return when {
            this > accelerometerTresholdMax -> accelerometerTresholdMax
            this < -accelerometerTresholdMax -> -accelerometerTresholdMax
            this in -accelerometerTresholdMin..accelerometerTresholdMin -> 0f
            else -> this
        }
    }

    private fun Float.mapToMotorSpeedRange(): Float {
        return if (this >= accelerometerTresholdMin) {
            mapRange(
                value = this,
                from1 = accelerometerTresholdMin,
                from2 = accelerometerTresholdMax,
                to1 = 0f,
                to2 = 255f,
            )
        } else if (this <= -accelerometerTresholdMin) {
            mapRange(
                value = this,
                from1 = -accelerometerTresholdMin,
                from2 = -accelerometerTresholdMax,
                to1 = 0f,
                to2 = -255f,
            )
        } else {
            0f
        }
    }

    private fun mapRange(value: Float, from1: Float, from2: Float, to1: Float, to2: Float): Float {
        return ((value - from1) / (from2 - from1) * (to2 - to1) + to1)
    }

    private fun calculateMotorSpeeds(x: Int, y: Int): Pair<Int, Int> {
        val leftMotor: Int
        val rightMotor: Int

        when {
            // Right
            x > 0 -> {
                when {
                    // 1st quadrant
                    y >= 0 -> {
                        leftMotor = max(x.absoluteValue, y.absoluteValue)
                        rightMotor = if (y - x > 0) y - x else 0
                    }

                    // 2th quadrant
                    else -> {
                        leftMotor = -max(x.absoluteValue, y.absoluteValue)
                        rightMotor = if (y + x < 0) y + x else 0
                    }
                }
            }

            // Left
            x < 0 -> {
                when {
                    // 4nd quadrant
                    y >= 0 -> {
                        leftMotor = if (y + x > 0) y + x else 0
                        rightMotor = max(x.absoluteValue, y.absoluteValue)
                    }

                    // 3rd quadrant
                    else -> {
                        leftMotor = if (y - x < 0) y - x else 0
                        rightMotor = -max(x.absoluteValue, y.absoluteValue)
                    }
                }
            }

            // Straight
            else -> {
                leftMotor = y
                rightMotor = y
            }
        }

        return leftMotor to rightMotor
    }
}
