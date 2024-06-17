package magym.robobt.repository.connect

sealed interface ConnectResult {

    data object Success : ConnectResult

    sealed interface Error : ConnectResult {

        data object SystemServiceNotExist : Error

        data object SystemServiceDisabled : Error

        data object PermissionNotGranted : Error

        data object NoPairedDevices : Error

        data object ConnectionFailed : Error
    }
}