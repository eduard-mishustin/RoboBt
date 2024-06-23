package magym.robobt.repository.connect

import kotlinx.coroutines.flow.Flow

interface ConnectRepository {

    /**
     * Enable the connection
     */
    fun enable()

    /**
     * Return a flow of [ConnectResult] that represent the connection status
     *
     * Auto reconnect if the connection failed
     */
    fun connect(): Flow<ConnectResult>

    /**
     * Return a flow of the received data
     *
     * Return null if the connection is not established
     */
    fun read(): Flow<ConnectionInputData?>

    /**
     * Return true if the [data] was sent successfully, false otherwise
     */
    fun send(data: String): Boolean

    /**
     * Disconnect the connection
     */
    fun disconnect()
}