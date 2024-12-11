package magym.robobt.video_stream

import android.graphics.Bitmap
import android.util.Log
import magym.robobt.video_stream.util.decodeMJPEGStream
import okhttp3.OkHttpClient
import okhttp3.Request

interface VideoStreamRepository {

    fun connect(callback: (Bitmap) -> Unit)
}

internal class VideoStreamRepositoryImpl(
    private val client: OkHttpClient,
) : VideoStreamRepository {

    override fun connect(callback: (Bitmap) -> Unit) {
        val request = Request.Builder()
            .url(VIDEO_STREAM_URL)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    response.body?.byteStream()?.let { inputStream ->
                        decodeMJPEGStream(inputStream) { bitmap ->
                            callback.invoke(bitmap)
                        }
                    }
                } else {
                    Log.e("Stream", "Connection error: ${response.code}")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {

        const val VIDEO_STREAM_URL = "http://192.168.31.208:81/stream"
    }
}