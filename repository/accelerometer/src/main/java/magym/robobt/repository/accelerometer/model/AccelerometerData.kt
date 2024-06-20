package magym.robobt.repository.accelerometer.model

data class AccelerometerData(
    val x: Float,
    val y: Float,
    val z: Float = 0f,
)