package magym.robobt.feature.control.presentation.tea.actor

import java.util.stream.Stream
import magym.robobt.feature.control.presentation.tea.model.ControlMotorsData
import magym.robobt.repository.accelerometer.model.AccelerometerData
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider

internal class MotorSpeedMapperProvider : ArgumentsProvider {

    override fun provideArguments(extensionContext: ExtensionContext): Stream<out Arguments> {
        return Stream.of(
            // Stop
            MotorSpeedMapperTestData(
                accelerometerData = AccelerometerData(x = 0f, y = 0f),
                motorsData = ControlMotorsData(leftMotor = 0, rightMotor = 0)
            ),
            MotorSpeedMapperTestData(
                accelerometerData = AccelerometerData(x = 1f, y = 1f),
                motorsData = ControlMotorsData(leftMotor = 0, rightMotor = 0)
            ),

            // 1st quadrant
            MotorSpeedMapperTestData(
                accelerometerData = AccelerometerData(x = 5f, y = 5f),
                motorsData = ControlMotorsData(leftMotor = 109, rightMotor = 0)
            ),
            MotorSpeedMapperTestData(
                accelerometerData = AccelerometerData(x = 5f, y = 3f),
                motorsData = ControlMotorsData(leftMotor = 109, rightMotor = 0)
            ),
            MotorSpeedMapperTestData(
                accelerometerData = AccelerometerData(x = 3f, y = 5f),
                motorsData = ControlMotorsData(leftMotor = 109, rightMotor = 73)
            ),

            // 2th quadrant
            MotorSpeedMapperTestData(
                accelerometerData = AccelerometerData(x = 5f, y = -5f),
                motorsData = ControlMotorsData(leftMotor = -109, rightMotor = 0)
            ),
            MotorSpeedMapperTestData(
                accelerometerData = AccelerometerData(x = 5f, y = -3f),
                motorsData = ControlMotorsData(leftMotor = -109, rightMotor = 0)
            ),
            MotorSpeedMapperTestData(
                accelerometerData = AccelerometerData(x = 3f, y = -5f),
                motorsData = ControlMotorsData(leftMotor = -109, rightMotor = -73)
            ),

            // 3rd quadrant
            MotorSpeedMapperTestData(
                accelerometerData = AccelerometerData(x = -5f, y = -5f),
                motorsData = ControlMotorsData(leftMotor = 0, rightMotor = -109)
            ),
            MotorSpeedMapperTestData(
                accelerometerData = AccelerometerData(x = -5f, y = -3f),
                motorsData = ControlMotorsData(leftMotor = 0, rightMotor = -109)
            ),
            MotorSpeedMapperTestData(
                accelerometerData = AccelerometerData(x = -3f, y = -5f),
                motorsData = ControlMotorsData(leftMotor = -73, rightMotor = -109)
            ),

            // 4nd quadrant
            MotorSpeedMapperTestData(
                accelerometerData = AccelerometerData(x = -5f, y = 5f),
                motorsData = ControlMotorsData(leftMotor = 0, rightMotor = 109)
            ),
            MotorSpeedMapperTestData(
                accelerometerData = AccelerometerData(x = -5f, y = 3f),
                motorsData = ControlMotorsData(leftMotor = 0, rightMotor = 109)
            ),
            MotorSpeedMapperTestData(
                accelerometerData = AccelerometerData(x = -3f, y = 5f),
                motorsData = ControlMotorsData(leftMotor = 73, rightMotor = 109)
            ),

            // Straight
            MotorSpeedMapperTestData(
                accelerometerData = AccelerometerData(x = 0f, y = 5f),
                motorsData = ControlMotorsData(leftMotor = 109, rightMotor = 109)
            ),
            MotorSpeedMapperTestData(
                accelerometerData = AccelerometerData(x = 0f, y = 9f),
                motorsData = ControlMotorsData(leftMotor = 255, rightMotor = 255)
            ),
            MotorSpeedMapperTestData(
                accelerometerData = AccelerometerData(x = 0f, y = -5f),
                motorsData = ControlMotorsData(leftMotor = -109, rightMotor = -109)
            ),
        ).map(Arguments::of)
    }
}