package magym.robobt.repository.connect.bluetooth

import kotlinx.coroutines.flow.Flow
import magym.robobt.repository.connect.bluetooth.model.BluetoothInputData
import magym.robobt.repository.connect.bluetooth.model.BluetoothResult

interface BluetoothRepository {

    /**
     * Enable the connection
     */
    fun enable()

    /**
     * Return a flow of [BluetoothResult] that represent the connection status
     *
     * Auto reconnect if the connection failed
     */
    fun connect(): Flow<BluetoothResult>

    /**
     * Return a flow of the received data
     *
     * Return null if the connection is not established
     */
    fun read(): Flow<BluetoothInputData?>

    /**
     * Return true if the [data] was sent successfully, false otherwise
     */
    fun send(data: String): Boolean

    /**
     * Disconnect the connection
     */
    fun disconnect()
}