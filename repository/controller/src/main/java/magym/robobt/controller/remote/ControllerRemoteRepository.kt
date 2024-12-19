package magym.robobt.controller.remote

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import magym.robobt.common.android.AppBuildConfig.BASE_REMOTE_CONTROLLER_URL
import magym.robobt.common.android.AppBuildConfig.IS_HOST
import magym.robobt.common.pure.model.ControlMotorsData
import magym.robobt.controller.ControllerRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

interface ControllerRemoteRepository : ControllerRepository

interface MutableControllerRemoteRepository : ControllerRemoteRepository {

    suspend fun send(data: ControlMotorsData): Boolean
}

internal class ControllerRemoteRepositoryImpl(
    private val okHttpClient: OkHttpClient,
    scope: CoroutineScope,
) : ControllerRemoteRepository, MutableControllerRemoteRepository {

    private val flow = MutableStateFlow(ControlMotorsData.empty())
    private var sendWebSocket: WebSocket? = null

    init {
        scope.launch {
            if (IS_HOST) {
                initHostWebSocket()
            } else {
                initRemoteWebSocket()
            }
        }
    }

    override fun connect(): Flow<ControlMotorsData> {
        return flow
    }

    override suspend fun send(data: ControlMotorsData): Boolean {
        return withContext(Dispatchers.IO) {
            val stringData = data.leftMotor.toString() + DELIMITER + data.rightMotor.toString()
            sendWebSocket?.send(stringData) ?: true
        }
    }

    private fun initHostWebSocket() {
        val request = Request.Builder().url("$BASE_REMOTE_CONTROLLER_URL/connect").build()

        okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                val leftMotor = text.substringBefore(DELIMITER)
                val rightMotor = text.substringAfter(DELIMITER)
                flow.tryEmit(ControlMotorsData(leftMotor.toInt(), rightMotor.toInt()))
            }
        })
    }

    private fun initRemoteWebSocket() {
        val request = Request.Builder().url("$BASE_REMOTE_CONTROLLER_URL/send").build()

        okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                sendWebSocket = webSocket
            }
        })
    }

    companion object {

        private const val DELIMITER = ":"
    }
}