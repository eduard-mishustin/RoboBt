package magym.robobt.controller.keyboard

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import magym.robobt.common.pure.model.ControlMotorsData
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
        update(rightMotor = 255)
    }

    override suspend fun onTopLeftButtonUp() {
        update(rightMotor = 0)
    }

    override suspend fun onTopRightButtonDown() {
        update(leftMotor = 255)
    }

    override suspend fun onTopRightButtonUp() {
        update(leftMotor = 0)
    }

    override suspend fun onBottomLeftButtonDown() {
        update(rightMotor = -255)
    }

    override suspend fun onBottomLeftButtonUp() {
        update(rightMotor = 0)
    }

    override suspend fun onBottomRightButtonDown() {
        update(leftMotor = -255)
    }

    override suspend fun onBottomRightButtonUp() {
        update(leftMotor = 0)
    }

    private suspend fun update(leftMotor: Int? = null, rightMotor: Int? = null) {
        val data = flow.value

        val newValue = data.copy(
            leftMotor = leftMotor ?: data.leftMotor,
            rightMotor = rightMotor ?: data.rightMotor,
        )

        flow.emit(newValue)
    }
}