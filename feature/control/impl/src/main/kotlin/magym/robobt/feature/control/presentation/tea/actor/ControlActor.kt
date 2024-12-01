package magym.robobt.feature.control.presentation.tea.actor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import magym.robobt.common.android.isRemote
import magym.robobt.common.android.url
import magym.robobt.common.pure.model.ControlMotorsData
import magym.robobt.common.tea.component.Actor
import magym.robobt.controller.accelerometer.ControllerAccelerometerRepository
import magym.robobt.controller.joystick.ControllerJoystickRepository
import magym.robobt.controller.joystick_triggers.ControllerJoystickTriggersRepository
import magym.robobt.controller.keyboard.ControllerKeyboardRepository
import magym.robobt.controller.web.ControllerWebRepository
import magym.robobt.feature.control.presentation.tea.core.ControlCommand
import magym.robobt.feature.control.presentation.tea.core.ControlCommand.ControlModeChanged
import magym.robobt.feature.control.presentation.tea.core.ControlEvent
import magym.robobt.feature.control.presentation.tea.core.ControlEvent.Controlling
import magym.robobt.feature.control.presentation.tea.model.ControlMode
import magym.robobt.repository.connect.bluetooth.BluetoothRepository
import magym.robobt.repository.connect.bluetooth.model.BluetoothOutputData
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException

internal class ControlActor(
    private val bluetoothRepository: BluetoothRepository,
    private val keyboardRepository: ControllerKeyboardRepository,
    private val accelerometerRepository: ControllerAccelerometerRepository,
    private val joystickRepository: ControllerJoystickRepository,
    private val joystickTriggersRepository: ControllerJoystickTriggersRepository,
    private val controllerWebRepository: ControllerWebRepository,
) : Actor<ControlCommand, ControlEvent> {

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
            controllerWebRepository.connect(),
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
                val client: OkHttpClient = OkHttpClient()

                // Формируем параметры для квери
                val urlBuilder: HttpUrl.Builder = url.toHttpUrlOrNull()?.newBuilder() ?: throw IllegalArgumentException("Invalid URL")
                urlBuilder.addQueryParameter("data", data.leftMotor.toString() + ":" + data.rightMotor.toString())
                val finalUrl: String = urlBuilder.build().toString()

                val formBody: RequestBody = FormBody.Builder()
                    .build()

                val request: Request = Request.Builder()
                    .url(finalUrl) // Используем URL с параметрами
                    .post(formBody)
                    .build()

                try {
                    val response: Response = client.newCall(request).execute()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
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