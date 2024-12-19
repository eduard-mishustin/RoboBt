package magym.robobt.video_stream

import android.graphics.Bitmap
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import magym.robobt.video_stream.util.decodeMJPEGStream
import okhttp3.OkHttpClient
import okhttp3.Request

interface VideoStreamRepository {

    fun connect(): Flow<Bitmap>

    fun closeConnection()
}

internal class VideoStreamRepositoryImpl(
    private val client: OkHttpClient,
) : VideoStreamRepository {

    override fun connect(): Flow<Bitmap> = callbackFlow {
        val request = Request.Builder()
            .url(VIDEO_STREAM_URL)
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
        }

        awaitClose {
            closeConnection()
        }
    }.flowOn(Dispatchers.IO)

    override fun closeConnection() {
        client.dispatcher.executorService.shutdown()
    }

    companion object {

        private const val VIDEO_STREAM_URL = "http://192.168.31.208:81/stream"
    }
}