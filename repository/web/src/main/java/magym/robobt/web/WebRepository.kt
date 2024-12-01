package magym.robobt.web

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

interface WebRepository {

    fun connect(): Flow<String>
}

internal class WebRepositoryImpl(
    private val okHttpClient: OkHttpClient,
    private val scope: CoroutineScope,
) : WebRepository {

    private val flow = MutableStateFlow("")

    override fun connect(): Flow<String> {
        return flow
    }

    init {
        scope.launch {
            val request = Request.Builder().url("$BASE_URL/connect").build()

            okHttpClient.newWebSocket(request, object : WebSocketListener() {
                override fun onMessage(webSocket: WebSocket, text: String) {
                    val text1 = text.removePrefix("Key: ")
                    flow.tryEmit(text1)
                }
            })
        }
    }

    companion object {

        private const val BASE_URL = "https://33b5-5-178-149-187.ngrok-free.app"
    }
}