package magym.robobt.repository.connect.bluetooth

import android.Manifest
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import magym.robobt.common.android.bluetoothDebug
import magym.robobt.repository.connect.bluetooth.impl.BluetoothConnection
import magym.robobt.repository.connect.bluetooth.impl.parser.BluetoothInputDataParser
import magym.robobt.repository.connect.bluetooth.impl.parser.BluetoothOutputDataParser
import magym.robobt.repository.connect.bluetooth.model.BluetoothConnectResult
import magym.robobt.repository.connect.bluetooth.model.BluetoothInputData
import magym.robobt.repository.connect.bluetooth.model.BluetoothOutputData
import kotlin.time.Duration.Companion.seconds

interface BluetoothRepository {

    /**
     * Enable the connection
     */
    fun enable()

    /**
     * Return a flow of [BluetoothConnectResult] that represent the connection status
     *
     * Auto reconnect if the connection failed
     */
    fun connect(): Flow<BluetoothConnectResult>

    /**
     * Return a flow of the received data
     *
     * Return null if the connection is not established
     */
    fun read(): Flow<BluetoothInputData?>

    /**
     * Return true if the [data] was sent successfully, false otherwise
     */
    suspend fun send(data: BluetoothOutputData): Boolean

    /**
     * Disconnect the connection
     */
    fun disconnect()
}

/**
 * https://developer.android.com/develop/connectivity/bluetooth/setup
 */
internal class BluetoothRepositoryImpl(
    private val context: Context,
    private val bluetoothManager: BluetoothManager,
    private val inputDataParser: BluetoothInputDataParser,
    private val outputDataParser: BluetoothOutputDataParser,
    private val dispatcher: CoroutineDispatcher,
) : BluetoothRepository {

    private val connection = BluetoothConnection()

    override fun enable() = TODO()

    override fun connect(): Flow<BluetoothConnectResult> = flow {
        while (true) {
            val result = withContext(dispatcher) { connectInternal() }
            println("BluetoothRepository.connect: $result")
            emit(result)

            if (result is BluetoothConnectResult.Success) break
            delay(1.seconds)
        }
    }

    override fun read(): Flow<BluetoothInputData?> = flow {
        while (true) {
            val pureData = withContext(dispatcher) { connection.read() }
            val result = inputDataParser.parse(pureData)

            if (pureData == null || result != null) {
                println("BluetoothRepository.read: $result")
                emit(result)
            }
        }
    }.distinctUntilChanged()

    override suspend fun send(data: BluetoothOutputData): Boolean {
        if (bluetoothDebug) {
            return true
        }

        println("BluetoothRepository.send: $data")
        return withContext(dispatcher) {
            val result = outputDataParser.parse(data)
            connection.write(result)
        }
    }

    override fun disconnect() {
        connection.cancel()
    }

    private fun connectInternal(): BluetoothConnectResult {
        if (bluetoothDebug) {
            return BluetoothConnectResult.Success
        }

        val adapter = bluetoothManager.adapter ?: return BluetoothConnectResult.Error.SystemServiceNotExist
        if (!adapter.isEnabled) return BluetoothConnectResult.Error.SystemServiceDisabled

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return BluetoothConnectResult.Error.PermissionNotGranted
        }

        val devices = adapter.bondedDevices
        if (devices.isEmpty()) return BluetoothConnectResult.Error.NoPairedDevices

        val roboModule = devices
            .find { it.name.contains(BLUETOOTH_MODULE_NAME) } ?: return BluetoothConnectResult.Error.NoPairedDevices

        val isSuccess = connection.connect(roboModule)

        if (!isSuccess) return BluetoothConnectResult.Error.ConnectionFailed
        return BluetoothConnectResult.Success
    }

    private companion object {

        const val BLUETOOTH_MODULE_NAME = "JDY-31"
    }
}