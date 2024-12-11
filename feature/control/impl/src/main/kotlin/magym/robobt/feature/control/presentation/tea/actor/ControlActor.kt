package magym.robobt.feature.control.presentation.tea.actor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import magym.robobt.common.android.BASE_URL
import magym.robobt.common.android.isRemote
import magym.robobt.common.pure.model.ControlMotorsData
import magym.robobt.common.tea.component.Actor
import magym.robobt.controller.accelerometer.ControllerAccelerometerRepository
import magym.robobt.controller.joystick.ControllerJoystickRepository
import magym.robobt.controller.joystick_triggers.ControllerJoystickTriggersRepository
import magym.robobt.controller.keyboard.ControllerKeyboardRepository
import magym.robobt.controller.remote.ControllerRemoteRepository
import magym.robobt.feature.control.presentation.tea.core.ControlCommand
import magym.robobt.feature.control.presentation.tea.core.ControlCommand.ControlModeChanged
import magym.robobt.feature.control.presentation.tea.core.ControlEvent
import magym.robobt.feature.control.presentation.tea.core.ControlEvent.Controlling
import magym.robobt.feature.control.presentation.tea.model.ControlMode
import magym.robobt.repository.connect.bluetooth.BluetoothRepository
import magym.robobt.repository.connect.bluetooth.model.BluetoothOutputData
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

internal class ControlActor(
    private val bluetoothRepository: BluetoothRepository,
    private val keyboardRepository: ControllerKeyboardRepository,
    private val accelerometerRepository: ControllerAccelerometerRepository,
    private val joystickRepository: ControllerJoystickRepository,
    private val joystickTriggersRepository: ControllerJoystickTriggersRepository,
    private val controllerRemoteRepository: ControllerRemoteRepository,
) : Actor<ControlCommand, ControlEvent> {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    var webSocketqwe: WebSocket? = null

    init {
        if (isRemote) {
            scope.launch {
                val okHttpClient = OkHttpClient()
                val request = Request.Builder().url("$BASE_URL/send").build()

                okHttpClient.newWebSocket(request, object : WebSocketListener() {
                    override fun onOpen(webSocket: WebSocket, response: Response) {
                        super.onOpen(webSocket, response)
                        webSocketqwe = webSocket
                    }
                })
            }
        }
    }

    override fun act(commands: Flow<ControlCommand>): Flow<ControlEvent> {
        return commands.filterIsInstance<ControlModeChanged>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: ControlModeChanged): Flow<Controlling> {
        println("ControlActor.handleCommand: $command")
        return when (command.mode) {
            ControlMode.Manual -> handleSendManualControlCommand()
            ControlMode.Accelerometer -> handleSubscribeToAccelerometerControlCommand()
        }
    }

    private fun handleSendManualControlCommand(): Flow<Controlling> {
        return combine(
            keyboardRepository.connect(),
            joystickRepository.connect(),
            joystickTriggersRepository.connect(),
            controllerRemoteRepository.connect(),
        ) { keyboard, joystick, joystickTriggers, web ->
            when {
                keyboard.isNotEmpty -> keyboard
                joystickTriggers.isNotEmpty -> joystickTriggers
                joystick.isNotEmpty -> joystick
                else -> web
            }
        }.map(::send)
    }

    private fun handleSubscribeToAccelerometerControlCommand(): Flow<Controlling> {
        return accelerometerRepository.connect()
            .map(::send)
    }

    private suspend fun send(data: ControlMotorsData): Controlling {
        if (isRemote) {
            withContext(Dispatchers.IO) {
                webSocketqwe?.send(data.leftMotor.toString() + ":" + data.rightMotor.toString())
            }
            return Controlling.Succeed(data)
        }

        val isSucceed = bluetoothRepository.send(data.toConnectData())
        return if (isSucceed) Controlling.Succeed(data) else Controlling.Failed
    }

    private fun ControlMotorsData.toConnectData(): BluetoothOutputData {
        return BluetoothOutputData.ControlMotorsData(
            leftMotor = leftMotor,
            rightMotor = rightMotor
        )
    }
}