package magym.robobt.controller

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface ControllerRepository {

    fun connect(): Flow<ControlMotorsData>
}

val joystickTriggersFlow = MutableStateFlow(ControlMotorsData.empty())
