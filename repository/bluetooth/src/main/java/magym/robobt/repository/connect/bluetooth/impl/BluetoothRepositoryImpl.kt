package magym.robobt.repository.connect.bluetooth.impl

import android.bluetooth.BluetoothManager
import android.content.Context
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import magym.robobt.repository.connect.bluetooth.BluetoothRepository
import magym.robobt.repository.connect.bluetooth.model.BluetoothInputData
import magym.robobt.repository.connect.bluetooth.model.BluetoothResult

/**
 * https://developer.android.com/develop/connectivity/bluetooth/setup
 */
internal class BluetoothRepositoryImpl(
    private val context: Context,
    private val bluetoothManager: BluetoothManager,
    private val inputDataParser: BluetoothInputDataParser,
    private val dispatcher: CoroutineDispatcher,
) : BluetoothRepository {

    private val connection = BluetoothConnection()

    override fun enable() {
        TODO()
    }

    override fun connect(): Flow<BluetoothResult> = flow {
        while (true) {
            val result = withContext(dispatcher) { connectInternal() }
            println("BluetoothRepository.connect: $result")
            emit(result)

            if (result is BluetoothResult.Success) break
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

    override fun send(data: String): Boolean {
        return connection.write(data)
    }

    override fun disconnect() {
        connection.cancel()
    }

    private fun connectInternal(): BluetoothResult {
        val adapter = bluetoothManager.adapter ?: return BluetoothResult.Error.SystemServiceNotExist
        if (!adapter.isEnabled) return BluetoothResult.Error.SystemServiceDisabled

        //        if (checkSelfPermission(context, BLUETOOTH_CONNECT) != PERMISSION_GRANTED) {
        //            return BluetoothResult.Error.PermissionNotGranted
        //        }

        val devices = adapter.bondedDevices
        if (devices.isEmpty()) return BluetoothResult.Error.NoPairedDevices

        val roboModule = devices
            .find { it.name.contains(BLUETOOTH_MODULE_NAME) } ?: return BluetoothResult.Error.NoPairedDevices

        val isSuccess = connection.connect(roboModule)

        if (!isSuccess) return BluetoothResult.Error.ConnectionFailed
        return BluetoothResult.Success
    }

    private companion object {

        const val BLUETOOTH_MODULE_NAME = "JDY-31"
    }
}