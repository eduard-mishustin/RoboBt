package magym.robobt.common.android.orientation

import android.content.pm.ActivityInfo
import magym.robobt.common.android.SingleActivityHolder

interface ScreenOrientationChanger {

    fun change(orientation: Orientation)

    companion object {

        fun create(singleActivityHolder: SingleActivityHolder): ScreenOrientationChanger {
            return ScreenOrientationChangerImpl(singleActivityHolder)
        }
    }
}

private class ScreenOrientationChangerImpl(
    private val singleActivityHolder: SingleActivityHolder,
) : ScreenOrientationChanger {

    private val Orientation.activityInfo: Int
        get() = when (this) {
            Orientation.Landscape -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            Orientation.Portrait -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

    override fun change(orientation: Orientation) {
        val oldOrientation = singleActivityHolder.requireActivity().requestedOrientation
        val newOrientation = orientation.activityInfo

        if (newOrientation != oldOrientation) {
            singleActivityHolder.requireActivity().requestedOrientation = newOrientation
        }
    }
}