package magym.robobt.repository.connect.bluetooth

import android.bluetooth.BluetoothManager
import kotlinx.coroutines.Dispatchers
import magym.robobt.repository.connect.bluetooth.impl.BluetoothInputDataParser
import magym.robobt.repository.connect.bluetooth.impl.BluetoothRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.dsl.onClose

val bluetoothModule = module {
    single<BluetoothRepository> {
        val context = androidContext()

        BluetoothRepositoryImpl(
            context = context,
            bluetoothManager = context.getSystemService(BluetoothManager::class.java),
            inputDataParser = BluetoothInputDataParser(),
            dispatcher = Dispatchers.IO,
        )

    } onClose { bluetoothRepository ->
        bluetoothRepository?.disconnect()
    }
}