package magym.robobt.controller.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import magym.robobt.common.pure.model.ControlMotorsData
import magym.robobt.controller.ControllerRepository
import magym.robobt.web.WebRepository

interface ControllerRemoteRepository : ControllerRepository

internal class ControllerRemoteRepositoryImpl(
    private val webRepository: WebRepository
) : ControllerRemoteRepository {

    private val flow = MutableStateFlow(ControlMotorsData.empty())

    override fun connect(): Flow<ControlMotorsData> {
        return webRepository.connect()
        /*.onEach { message ->
            when (message) {
                TopLeftButtonDown -> onTopLeftButtonDown()
                TopLeftButtonUp -> onTopLeftButtonUp()
                TopRightButtonDown -> onTopRightButtonDown()
                TopRightButtonUp -> onTopRightButtonUp()
                BottomLeftButtonDown -> onBottomLeftButtonDown()
                BottomLeftButtonUp -> onBottomLeftButtonUp()
                BottomRightButtonDown -> onBottomRightButtonDown()
                BottomRightButtonUp -> onBottomRightButtonUp()
            }
        }.flatMapLatest { flow }*/
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