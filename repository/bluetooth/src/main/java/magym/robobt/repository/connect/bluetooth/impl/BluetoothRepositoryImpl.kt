package magym.robobt.repository.connect.bluetooth.impl

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.ActivityCompat.checkSelfPermission
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import magym.robobt.repository.connect.bluetooth.BluetoothRepository
import magym.robobt.repository.connect.bluetooth.impl.parser.BluetoothInputDataParser
import magym.robobt.repository.connect.bluetooth.impl.parser.BluetoothOutputDataParser
import magym.robobt.repository.connect.bluetooth.model.BluetoothConnectResult
import magym.robobt.repository.connect.bluetooth.model.BluetoothInputData
import magym.robobt.repository.connect.bluetooth.model.BluetoothOutputData

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
        val adapter = bluetoothManager.adapter ?: return BluetoothConnectResult.Error.SystemServiceNotExist
        if (!adapter.isEnabled) return BluetoothConnectResult.Error.SystemServiceDisabled

        if (checkSelfPermission(context, BLUETOOTH_CONNECT) != PERMISSION_GRANTED) {
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