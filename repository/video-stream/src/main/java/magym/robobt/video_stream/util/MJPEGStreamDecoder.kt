package magym.robobt.video_stream.util

import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.io.InputStream

private const val MJPEG_BOUNDARY = "--123456789000000000000987654321"

internal fun decodeMJPEGStream(inputStream: InputStream, onFrameDecoded: (android.graphics.Bitmap) -> Unit) {
    val boundary = MJPEG_BOUNDARY.toByteArray()
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

/**
 * Поиска подстроки в массиве байтов
 */
private fun ByteArray.indexOfSlice(slice: ByteArray, startIndex: Int = 0, endIndex: Int = this.size): Int {
    if (slice.isEmpty() || slice.size > this.size) return -1
    for (i in startIndex until (endIndex - slice.size + 1)) {
        if (this.sliceArray(i until i + slice.size).contentEquals(slice)) {
            return i
        }
    }
    return -1
}