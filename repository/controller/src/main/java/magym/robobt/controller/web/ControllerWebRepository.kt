package magym.robobt.controller.web

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import magym.robobt.controller.ControlMotorsData
import magym.robobt.controller.ControllerRepository
import magym.robobt.web.WebRepository

interface ControllerWebRepository : ControllerRepository

internal class ControllerWebRepositoryImpl(
    private val webRepository: WebRepository
) : ControllerWebRepository {

    private val flow = MutableStateFlow(ControlMotorsData.empty())

    override fun connect(): Flow<ControlMotorsData> {
        return webRepository.connect()
            .onEach { message ->
                when (message) {
                    "F:true" -> onTopLeftButtonDown()
                    "F:false" -> onTopLeftButtonUp()
                    "K:true" -> onTopRightButtonDown()
                    "K:false" -> onTopRightButtonUp()
                    "V:true" -> onBottomLeftButtonDown()
                    "V:false" -> onBottomLeftButtonUp()
                    "M:true" -> onBottomRightButtonDown()
                    "M:false" -> onBottomRightButtonUp()
                    else -> Unit
                }
            }.flatMapLatest { flow }
    }

    private suspend fun onTopLeftButtonDown() {
        val data = flow.value
        flow.emit(data.copy(rightMotor = 255))
    }

    private suspend fun onTopLeftButtonUp() {
        val data = flow.value
        flow.emit(data.copy(rightMotor = 0))
    }

    private suspend fun onTopRightButtonDown() {
        val data = flow.value
        flow.emit(data.copy(leftMotor = 255))
    }

    private suspend fun onTopRightButtonUp() {
        val data = flow.value
        flow.emit(data.copy(leftMotor = 0))
    }

    private suspend fun onBottomLeftButtonDown() {
        val data = flow.value
        flow.emit(data.copy(rightMotor = -255))
    }

    private suspend fun onBottomLeftButtonUp() {
        val data = flow.value
        flow.emit(data.copy(rightMotor = 0))
    }

    private suspend fun onBottomRightButtonDown() {
        val data = flow.value
        flow.emit(data.copy(leftMotor = -255))
    }

    private suspend fun onBottomRightButtonUp() {
        val data = flow.value
        flow.emit(data.copy(leftMotor = 0))
    }
}