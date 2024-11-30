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
        val data = flow.value
        flow.emit(data.copy(rightMotor = 255))
    }

    override suspend fun onTopLeftButtonUp() {
        val data = flow.value
        flow.emit(data.copy(rightMotor = 0))
    }

    override suspend fun onTopRightButtonDown() {
        val data = flow.value
        flow.emit(data.copy(leftMotor = 255))
    }

    override suspend fun onTopRightButtonUp() {
        val data = flow.value
        flow.emit(data.copy(leftMotor = 0))
    }

    override suspend fun onBottomLeftButtonDown() {
        val data = flow.value
        flow.emit(data.copy(rightMotor = -255))
    }

    override suspend fun onBottomLeftButtonUp() {
        val data = flow.value
        flow.emit(data.copy(rightMotor = 0))
    }

    override suspend fun onBottomRightButtonDown() {
        val data = flow.value
        flow.emit(data.copy(leftMotor = -255))
    }

    override suspend fun onBottomRightButtonUp() {
        val data = flow.value
        flow.emit(data.copy(leftMotor = 0))
    }
}