package magym.robobt.controller

import kotlinx.coroutines.flow.Flow
import magym.robobt.common.pure.model.ControlMotorsData

interface ControllerRepository {

    fun connect(): Flow<ControlMotorsData>
}