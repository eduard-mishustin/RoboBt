package magym.robobt

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import magym.robobt.common.android.SingleActivityHolder
import magym.robobt.common.navigation.voyager.impl.NavigatorHolder
import magym.robobt.common.ui.theme.RoboTheme
import magym.robobt.feature.connect.ConnectScreenProvider
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.android.ext.android.inject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

class AppActivity : ComponentActivity() {

    private val singleActivityHolder: SingleActivityHolder by inject()
    private val navigatorHolder: NavigatorHolder by inject()
    private val connectScreenProvider: ConnectScreenProvider by inject()
    private val activityGenericMotionDelegate: ActivityGenericMotionDelegate by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        singleActivityHolder.activity = WeakReference(this)

        enableEdgeToEdge()

        setContent {
            RoboTheme {
                UsualNavigator()
            }
        }
    }

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        val result = activityGenericMotionDelegate.onGenericMotionEvent(event)
        return if (result) true else super.onGenericMotionEvent(event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val result = activityGenericMotionDelegate.onKeyDown(keyCode, event)
        return if (result) true else super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        val result = activityGenericMotionDelegate.onKeyUp(keyCode, event)
        return if (result) true else super.onKeyUp(keyCode, event)
    }

    @Composable
    private fun UsualNavigator() {
        Navigator(
            screen = connectScreenProvider() as Screen,
            onBackPressed = { false },
        ) { navigator ->
            navigatorHolder.usualNavigator = navigator

            SlideTransition(navigator = navigator) { screen ->
                screen.Content()
            }
            VideoStreamView()
        }
    }


    @Composable
    fun VideoStreamView() {
        //val videoUrl = "https://46ff-5-178-149-187.ngrok-free.app/stream"
        val videoUrl = "http://192.168.31.208:81/stream"
        MJPEGStreamScreen(streamUrl = videoUrl)
    }

    @Composable
    fun MJPEGStreamScreen(streamUrl: String) {
        var currentFrame by remember { mutableStateOf<ImageBitmap?>(null) }

        LaunchedEffect(streamUrl) {
            val client = OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build()
            val request = Request.Builder()
                .url(streamUrl)
                .build()

            withContext(Dispatchers.IO) {
                try {
                    client.newCall(request).execute().use { response ->
                        if (response.isSuccessful) {
                            response.body?.byteStream()?.let { inputStream ->
                                decodeMJPEGStream(inputStream) { bitmap ->
                                    currentFrame = bitmap.asImageBitmap()
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
        }

        if (currentFrame != null) {
            Image(
                bitmap = currentFrame!!,
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    private fun decodeMJPEGStream(inputStream: InputStream, onFrameDecoded: (android.graphics.Bitmap) -> Unit) {
        val boundary = "--123456789000000000000987654321".toByteArray()
        val buffer = ByteArray(1024 * 8)
        var bytesRead: Int
        var dataBuffer = ByteArrayOutputStream()

        try {
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                dataBuffer.write(buffer, 0, bytesRead)
                val data = dataBuffer.toByteArray()

                // Найти разделитель
                val splitIndex = data.indexOfSlice(boundary)
                if (splitIndex != -1) {
                    // Извлечь JPEG между boundary
                    val frameStart = data.indexOfSlice("\r\n\r\n".toByteArray(), 0, splitIndex) + 4
                    if (frameStart > 4 && frameStart < splitIndex) {
                        val frameData = data.sliceArray(frameStart until splitIndex)
                        val frame = BitmapFactory.decodeByteArray(frameData, 0, frameData.size)
                        if (frame != null) {
                            onFrameDecoded(frame)
                        }
                    }

                    // Сбросить буфер, оставив данные после boundary
                    dataBuffer = ByteArrayOutputStream()
                    dataBuffer.write(data, splitIndex + boundary.size, data.size - splitIndex - boundary.size)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            dataBuffer.close()
            inputStream.close()
        }
    }

    // Функция для поиска подстроки в массиве байтов
    private fun ByteArray.indexOfSlice(slice: ByteArray, startIndex: Int = 0, endIndex: Int = this.size): Int {
        if (slice.isEmpty() || slice.size > this.size) return -1
        for (i in startIndex until (endIndex - slice.size + 1)) {
            if (this.sliceArray(i until i + slice.size).contentEquals(slice)) {
                return i
            }
        }
        return -1
    }
}