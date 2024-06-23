package magym.robobt.repository.connect.bluetooth

import magym.robobt.repository.connect.ConnectionInputData

class ConnectionInputDataParser {

    fun parse(data: String?): ConnectionInputData? {
        if (data.isNullOrBlank() || data.isEmpty() || data.length < 3) return null
        val params = data.replace("\n", ";").split(";")

        return ConnectionInputData(
            temperature = params[0].toDouble(),
            humidity = params[1].toDouble(),
        )
    }
}