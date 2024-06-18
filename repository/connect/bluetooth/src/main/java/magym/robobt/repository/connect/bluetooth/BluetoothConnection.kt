package magym.robobt.repository.connect.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

@SuppressLint("MissingPermission")
internal class BluetoothConnection {

    private val uuid: UUID = UUID.fromString(UUID_STRING_WELL_KNOWN_SPP)

    private var socket: BluetoothSocket? = null
    private var connectedOutputStream: OutputStream? = null

    fun connect(device: BluetoothDevice): Boolean {
        try {
            cancel()
            socket = device.createRfcommSocketToServiceRecord(uuid)
            val socket = socket ?: return false

            try {
                socket.connect()
            } catch (e: IOException) {
                e.printStackTrace()
                cancel()
                return false
            }

            connectedOutputStream = socket.outputStream
        } catch (e: IOException) {
            e.printStackTrace()
            cancel()
            return false
        }

        return true
    }

    fun write(data: String): Boolean {
        try {
            val buffer = data.toByteArray()
            connectedOutputStream?.write(buffer)
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            cancel()
            return false
        }
    }

    fun cancel() {
        try {
            socket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private companion object {

        const val UUID_STRING_WELL_KNOWN_SPP = "00001101-0000-1000-8000-00805F9B34FB"
    }
}