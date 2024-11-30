package magym.robobt.repository.accelerometer

import magym.robobt.repository.input_device.InputDeviceData
import magym.robobt.repository.input_device.accelerometer.model.ControlMotorsData
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

internal class MotorSpeedMapperProvider : ArgumentsProvider {

    override fun provideArguments(extensionContext: ExtensionContext): Stream<out Arguments> {
        return Stream.of(
            // Stop
            MotorSpeedMapperTestData(
                inputDeviceData = InputDeviceData(x = 0f, y = 0f),
                motorsData = ControlMotorsData(leftMotor = 0, rightMotor = 0)
            ),
            MotorSpeedMapperTestData(
                inputDeviceData = InputDeviceData(x = 1f, y = 1f),
                motorsData = ControlMotorsData(leftMotor = 0, rightMotor = 0)
            ),

            // 1st quadrant
            MotorSpeedMapperTestData(
                inputDeviceData = InputDeviceData(x = 5f, y = 5f),
                motorsData = ControlMotorsData(leftMotor = 109, rightMotor = 0)
            ),
            MotorSpeedMapperTestData(
                inputDeviceData = InputDeviceData(x = 5f, y = 3f),
                motorsData = ControlMotorsData(leftMotor = 109, rightMotor = 0)
            ),
            MotorSpeedMapperTestData(
                inputDeviceData = InputDeviceData(x = 3f, y = 5f),
                motorsData = ControlMotorsData(leftMotor = 109, rightMotor = 73)
            ),

            // 2th quadrant
            MotorSpeedMapperTestData(
                inputDeviceData = InputDeviceData(x = 5f, y = -5f),
                motorsData = ControlMotorsData(leftMotor = -109, rightMotor = 0)
            ),
            MotorSpeedMapperTestData(
                inputDeviceData = InputDeviceData(x = 5f, y = -3f),
                motorsData = ControlMotorsData(leftMotor = -109, rightMotor = 0)
            ),
            MotorSpeedMapperTestData(
                inputDeviceData = InputDeviceData(x = 3f, y = -5f),
                motorsData = ControlMotorsData(leftMotor = -109, rightMotor = -73)
            ),

            // 3rd quadrant
            MotorSpeedMapperTestData(
                inputDeviceData = InputDeviceData(x = -5f, y = -5f),
                motorsData = ControlMotorsData(leftMotor = 0, rightMotor = -109)
            ),
            MotorSpeedMapperTestData(
                inputDeviceData = InputDeviceData(x = -5f, y = -3f),
                motorsData = ControlMotorsData(leftMotor = 0, rightMotor = -109)
            ),
            MotorSpeedMapperTestData(
                inputDeviceData = InputDeviceData(x = -3f, y = -5f),
                motorsData = ControlMotorsData(leftMotor = -73, rightMotor = -109)
            ),

            // 4nd quadrant
            MotorSpeedMapperTestData(
                inputDeviceData = InputDeviceData(x = -5f, y = 5f),
                motorsData = ControlMotorsData(leftMotor = 0, rightMotor = 109)
            ),
            MotorSpeedMapperTestData(
                inputDeviceData = InputDeviceData(x = -5f, y = 3f),
                motorsData = ControlMotorsData(leftMotor = 0, rightMotor = 109)
            ),
            MotorSpeedMapperTestData(
                inputDeviceData = InputDeviceData(x = -3f, y = 5f),
                motorsData = ControlMotorsData(leftMotor = 73, rightMotor = 109)
            ),

            // Straight
            MotorSpeedMapperTestData(
                inputDeviceData = InputDeviceData(x = 0f, y = 5f),
                motorsData = ControlMotorsData(leftMotor = 109, rightMotor = 109)
            ),
            MotorSpeedMapperTestData(
                inputDeviceData = InputDeviceData(x = 0f, y = 9f),
                motorsData = ControlMotorsData(leftMotor = 255, rightMotor = 255)
            ),
            MotorSpeedMapperTestData(
                inputDeviceData = InputDeviceData(x = 0f, y = -5f),
                motorsData = ControlMotorsData(leftMotor = -109, rightMotor = -109)
            ),
        ).map(Arguments::of)
    }
}