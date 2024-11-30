package magym.robobt.controller.keyboard

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import magym.robobt.controller.ControlMotorsData
import magym.robobt.controller.ControllerRepository

interface ControllerKeyboardRepository : ControllerRepository

interface MutableControllerKeyboardRepository : ControllerKeyboardRepository {

    suspend fun onTopLeftButtonDown()

    suspend fun onTopLeftButtonUp()

    suspend fun onTopRightButtonDown()

    suspend fun onTopRightButtonUp()

    suspend fun onBottomLeftButtonDown()

    suspend fun onBottomLeftButtonUp()

    suspend fun onBottomRightButtonDown()

    suspend fun onBottomRightButtonUp()
}

internal class ControllerKeyboardRepositoryImpl : ControllerKeyboardRepository, MutableControllerKeyboardRepository {

    private val flow = MutableStateFlow(ControlMotorsData.empty())

    override fun connect(): Flow<ControlMotorsData> {
        return flow
    }

    override suspend fun onTopLeftButtonDown() {
        flow.emit(flow.value.copy(rightMotor = 255))
    }

    override suspend fun onTopLeftButtonUp() {
        flow.emit(flow.value.copy(rightMotor = 0))
    }

    override suspend fun onTopRightButtonDown() {
        flow.emit(flow.value.copy(leftMotor = 255))
    }

    override suspend fun onTopRightButtonUp() {
        flow.emit(flow.value.copy(leftMotor = 0))
    }

    override suspend fun onBottomLeftButtonDown() {
        flow.emit(flow.value.copy(rightMotor = -255))
    }

    override suspend fun onBottomLeftButtonUp() {
        flow.emit(flow.value.copy(rightMotor = 0))
    }

    override suspend fun onBottomRightButtonDown() {
        flow.emit(flow.value.copy(leftMotor = -255))
    }

    override suspend fun onBottomRightButtonUp() {
        flow.emit(flow.value.copy(leftMotor = 0))
    }
}