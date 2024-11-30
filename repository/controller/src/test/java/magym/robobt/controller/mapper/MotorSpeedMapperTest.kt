package magym.robobt.controller.mapper

import io.kotest.matchers.shouldBe
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

internal class MotorSpeedMapperTest {

    private val mapper = MotorSpeedMapper(accelerometerTresholdMin = 2F, accelerometerTresholdMax = 9F)

    @ParameterizedTest
    @ArgumentsSource(MotorSpeedMapperProvider::class)
    fun testMotorSpeedMapper(testData: MotorSpeedMapperTestData) {
        val result = mapper.map(testData.inputDeviceData)
        result shouldBe testData.motorsData
    }
}