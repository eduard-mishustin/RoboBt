package magym.robobt.repository.connect.bluetooth.impl.parser

import magym.robobt.repository.connect.bluetooth.model.BluetoothOutputData

internal class BluetoothOutputDataParser {

    private val BluetoothOutputData.tag: String
        get() = when (this) {
            is BluetoothOutputData.ControlMotorsData -> "m"
        }

    fun parse(data: BluetoothOutputData): String {
        return when (data) {
            is BluetoothOutputData.ControlMotorsData -> {
                "${data.tag}${data.leftMotor}$DELIMITER${data.rightMotor}$DELIMITER"
            }
        }
    }

    private companion object {

        const val DELIMITER = ";"
    }
}