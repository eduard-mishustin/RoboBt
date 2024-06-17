package magym.robobt.repository.connect.bluetooth

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.ActivityCompat.checkSelfPermission
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import magym.robobt.repository.connect.ConnectRepository
import magym.robobt.repository.connect.ConnectResult

/**
 * https://developer.android.com/develop/connectivity/bluetooth/setup
 */
class BluetoothRepository(
    private val context: Context,
    private val bluetoothManager: BluetoothManager,
    private val dispatcher: CoroutineDispatcher,
) : ConnectRepository {

    private val connection = BluetoothConnection()

    override fun enable() {
        TODO()
    }

    override fun connect(): Flow<ConnectResult> = flow {
        while (true) {
            val result = withContext(dispatcher) { connectInternal() }
            println("BluetoothRepository.connect: $result")
            emit(result)

            if (result is ConnectResult.Success) break
            delay(1.seconds)
        }
    }

    override fun write(data: String): Boolean {
        return connection.write(data)
    }

    override fun disconnect() {
        connection.cancel()
    }

    private fun connectInternal(): ConnectResult {
        val adapter = bluetoothManager.adapter ?: return ConnectResult.Error.SystemServiceNotExist
        if (!adapter.isEnabled) return ConnectResult.Error.SystemServiceDisabled

        if (checkSelfPermission(context, BLUETOOTH_CONNECT) != PERMISSION_GRANTED) {
            return ConnectResult.Error.PermissionNotGranted
        }

        val devices = adapter.bondedDevices
        if (devices.isEmpty()) return ConnectResult.Error.NoPairedDevices

        val roboModule = devices
            .find { it.name.contains(BLUETOOTH_MODULE_NAME) } ?: return ConnectResult.Error.NoPairedDevices

        val isSuccess = connection.connect(roboModule)

        if (!isSuccess) return ConnectResult.Error.ConnectionFailed
        return ConnectResult.Success
    }

    private companion object {

        const val BLUETOOTH_MODULE_NAME = "JDY-31"
    }
}