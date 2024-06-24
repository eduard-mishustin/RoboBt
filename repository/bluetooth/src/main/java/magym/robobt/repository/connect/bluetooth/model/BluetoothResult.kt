package magym.robobt.repository.connect.bluetooth.model

sealed interface BluetoothResult {

    data object Success : BluetoothResult

    sealed interface Error : BluetoothResult {

        data object SystemServiceNotExist : Error

        data object SystemServiceDisabled : Error

        data object PermissionNotGranted : Error

        data object NoPairedDevices : Error

        data object ConnectionFailed : Error
    }
}