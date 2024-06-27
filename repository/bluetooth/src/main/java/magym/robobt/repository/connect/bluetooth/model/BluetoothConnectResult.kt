package magym.robobt.repository.connect.bluetooth.model

sealed interface BluetoothConnectResult {

    data object Success : BluetoothConnectResult

    sealed interface Error : BluetoothConnectResult {

        data object SystemServiceNotExist : Error

        data object SystemServiceDisabled : Error

        data object PermissionNotGranted : Error

        data object NoPairedDevices : Error

        data object ConnectionFailed : Error
    }
}