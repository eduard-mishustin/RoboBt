package magym.robobt.common.navigation.voyager.impl

import magym.robobt.common.android.SingleActivityHolder

internal interface ApplicationExitProvider {

    fun exit()
}

internal class AndroidApplicationExitProvider(
    private val activityHolder: SingleActivityHolder,
) : ApplicationExitProvider {

    override fun exit() {
        activityHolder.activity.moveTaskToBack(true)
    }
}