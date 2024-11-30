package magym.robobt.controller.keyboard

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import magym.robobt.controller.ControlMotorsData
import magym.robobt.controller.ControllerRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

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

val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

internal class ControllerKeyboardRepositoryImpl : ControllerKeyboardRepository, MutableControllerKeyboardRepository {

    private val flow = MutableStateFlow(ControlMotorsData.empty())

    init {
        scope.launch {
            val request = Request.Builder().url("https://b369-5-178-149-187.ngrok-free.app/connect").build()

            val webSocket = OkHttpClient().newWebSocket(request, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    println("wdqewfrgethr onOpen")
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    println("wdqewfrgethr onFailure $response")
                    t.printStackTrace()
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    println("wdqewfrgethr onClosing $code, $reason")
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    println("wdqewfrgethr onClosed")
                }

                override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                    println("wdqewfrgethr onMessage $bytes")
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    val text1 = text.removePrefix("Key: ")
                    println("wdqewfrgethr onMessage $text1")
                    scope.launch {
                        when (text1) {
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
                    }
                }
            })
        }
    }

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