package magym.robobt.repository.connect.bluetooth

import android.bluetooth.BluetoothManager
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.dsl.onClose

val bluetoothModule = module {
    single {
        val context = androidContext()

        BluetoothRepository(
            context = context,
            bluetoothManager = context.getSystemService(BluetoothManager::class.java),
            inputDataParser = ConnectionInputDataParser(),
            dispatcher = Dispatchers.IO,
        )

    } onClose { bluetoothRepository ->
        bluetoothRepository?.disconnect()
    }
}