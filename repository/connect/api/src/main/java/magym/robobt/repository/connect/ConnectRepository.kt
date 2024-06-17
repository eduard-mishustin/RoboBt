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
     * Return true if the [data] was written successfully, false otherwise
     */
    fun write(data: String): Boolean

    /**
     * Disconnect the connection
     */
    fun disconnect()
}