package magym.robobt.controller.remote

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import magym.robobt.common.android.BASE_URL
import magym.robobt.common.android.isRemote
import magym.robobt.common.pure.model.ControlMotorsData
import magym.robobt.controller.ControllerRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

interface ControllerRemoteRepository : ControllerRepository

internal class ControllerRemoteRepositoryImpl(
    private val okHttpClient: OkHttpClient,
    scope: CoroutineScope,
) : ControllerRemoteRepository {

    private val flow = MutableStateFlow(ControlMotorsData.empty())

    init {
        if (!isRemote) {
            scope.launch {
                val request = Request.Builder().url("$BASE_URL/connect").build()

                okHttpClient.newWebSocket(request, object : WebSocketListener() {
                    override fun onMessage(webSocket: WebSocket, text: String) {
                        val leftMotor = text.substringBefore(":")
                        val rightMotor = text.substringAfter(":")
                        flow.tryEmit(ControlMotorsData(leftMotor.toInt(), rightMotor.toInt()))
                    }
                })
            }
        }
    }

    override fun connect(): Flow<ControlMotorsData> {
        return flow
    }
}