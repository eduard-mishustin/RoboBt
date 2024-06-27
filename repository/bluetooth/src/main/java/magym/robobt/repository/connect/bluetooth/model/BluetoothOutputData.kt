package magym.robobt.repository.connect.bluetooth.model

sealed interface BluetoothOutputData {

    data class ControlMotorsData(
        val leftMotor: Int,
        val rightMotor: Int,
    ) : BluetoothOutputData
}