package magym.robobt.controller.joystick

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import magym.robobt.common.pure.model.ControlMotorsData
import magym.robobt.common.pure.util.startWith
import magym.robobt.controller.ControllerRepository
import magym.robobt.controller.mapper.MotorSpeedMapper
import magym.robobt.repository.input_device.joystick.JoystickRepository

interface ControllerJoystickRepository : ControllerRepository

internal class ControllerJoystickRepositoryImpl(
    private val joystickRepository: JoystickRepository,
    private val motorSpeedMapper: MotorSpeedMapper,
) : ControllerJoystickRepository {

    override fun connect(): Flow<ControlMotorsData> {
        return joystickRepository.connect()
            .map(motorSpeedMapper::map)
            .startWith(ControlMotorsData.empty())
            .distinctUntilChanged()
    }
}