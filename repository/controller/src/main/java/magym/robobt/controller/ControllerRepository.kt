package magym.robobt.controller

import kotlinx.coroutines.flow.Flow

interface ControllerRepository {

    fun connect(): Flow<ControlMotorsData>
}