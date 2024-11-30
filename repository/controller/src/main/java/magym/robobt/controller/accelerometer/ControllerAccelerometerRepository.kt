package magym.robobt.controller.accelerometer

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import magym.robobt.controller.ControlMotorsData
import magym.robobt.controller.ControllerRepository
import magym.robobt.controller.mapper.MotorSpeedMapper
import magym.robobt.repository.input_device.accelerometer.AccelerometerRepository

interface ControllerAccelerometerRepository : ControllerRepository

internal class ControllerAccelerometerRepositoryImpl(
    private val accelerometerRepository: AccelerometerRepository,
    private val motorSpeedMapper: MotorSpeedMapper,
) : ControllerAccelerometerRepository {

    override fun connect(): Flow<ControlMotorsData> {
        return accelerometerRepository.connect()
            .map(motorSpeedMapper::map)
            .distinctUntilChanged()
    }
}