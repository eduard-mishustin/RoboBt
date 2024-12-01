package magym.robobt.web

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import magym.robobt.common.android.isRemote
import magym.robobt.common.android.url
import magym.robobt.common.pure.model.ControlMotorsData
import magym.robobt.web.WebResponse.BottomLeftButtonDown
import magym.robobt.web.WebResponse.BottomLeftButtonUp
import magym.robobt.web.WebResponse.BottomRightButtonDown
import magym.robobt.web.WebResponse.BottomRightButtonUp
import magym.robobt.web.WebResponse.TopLeftButtonDown
import magym.robobt.web.WebResponse.TopLeftButtonUp
import magym.robobt.web.WebResponse.TopRightButtonDown
import magym.robobt.web.WebResponse.TopRightButtonUp
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

interface WebRepository {

    fun connect(): Flow<ControlMotorsData>
}

internal class WebRepositoryImpl(
    private val okHttpClient: OkHttpClient,
    private val scope: CoroutineScope,
) : WebRepository {

    private val flow: MutableStateFlow<ControlMotorsData> = MutableStateFlow(ControlMotorsData.empty())

    override fun connect(): Flow<ControlMotorsData> {
        return flow
    }

    init {
        if (!isRemote) {
            scope.launch {
                val request = Request.Builder().url("$BASE_URL/connect").build()

                okHttpClient.newWebSocket(request, object : WebSocketListener() {
                    override fun onMessage(webSocket: WebSocket, text: String) {
                        val l = text.substringBefore(":")
                        val r = text.substringAfter(":")
                        flow.tryEmit(ControlMotorsData(l.toInt(), r.toInt()))
                    }
                })
            }
        }
    }

    private fun mapMessage(message: String): WebResponse? {
        return when (message) {
            "F:true" -> TopLeftButtonDown
            "F:false" -> TopLeftButtonUp
            "K:true" -> TopRightButtonDown
            "K:false" -> TopRightButtonUp
            "V:true" -> BottomLeftButtonDown
            "V:false" -> BottomLeftButtonUp
            "M:true" -> BottomRightButtonDown
            "M:false" -> BottomRightButtonUp
            else -> null
        }
    }

    companion object {

        private const val BASE_URL = url //"https://a78a-5-178-149-187.ngrok-free.app"
    }
}