package magym.robobt.video_stream

import android.graphics.Bitmap
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOn
import magym.robobt.common.android.AppBuildConfig.BASE_VIDEO_STREAM_URL
import magym.robobt.common.android.AppBuildConfig.IS_VIDEO_STREAM_ENABLED
import magym.robobt.video_stream.util.decodeMJPEGStream
import okhttp3.OkHttpClient
import okhttp3.Request

interface VideoStreamRepository {

    fun connect(onConnectionError: () -> Unit): Flow<Bitmap>

    fun closeConnection()
}

internal class VideoStreamRepositoryImpl(
    private val client: OkHttpClient,
) : VideoStreamRepository {

    override fun connect(onConnectionError: () -> Unit): Flow<Bitmap> {
        if (!IS_VIDEO_STREAM_ENABLED) {
            return emptyFlow()
        }

        return callbackFlow {
            val request = Request.Builder()
                .url("$BASE_VIDEO_STREAM_URL/stream")
                .build()

            try {
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        response.body?.byteStream()?.let { inputStream ->
                            decodeMJPEGStream(inputStream) { bitmap ->
                                trySend(bitmap)
                            }
                        }
                    } else {
                        Log.e("Stream", "Connection error: ${response.code}")
                    }
                }
            } catch (e: Exception) {
                closeConnection()
                e.printStackTrace()
                onConnectionError.invoke()
            }

            awaitClose { closeConnection() }
        }.flowOn(Dispatchers.IO)
    }

    override fun closeConnection() {
        client.dispatcher.executorService.shutdown()
    }
}