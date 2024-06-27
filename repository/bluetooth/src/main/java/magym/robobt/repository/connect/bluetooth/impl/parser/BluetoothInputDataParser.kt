package magym.robobt.repository.connect.bluetooth.impl.parser

import magym.robobt.repository.connect.bluetooth.model.BluetoothInputData

internal class BluetoothInputDataParser {

    fun parse(data: String?): BluetoothInputData? {
        if (data.isNullOrBlank() || data.isEmpty() || data.length < 3) return null
        val params = data.replace("\n", ";").split(";")

        return try {
            BluetoothInputData(
                temperature = params[0].toDouble(),
                humidity = params[1].toDouble(),
            )
        } catch (e: NumberFormatException) {
            null
        }
    }
}