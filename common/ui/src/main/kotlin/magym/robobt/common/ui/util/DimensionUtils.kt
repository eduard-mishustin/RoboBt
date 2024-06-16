package magym.robobt.common.ui.util

import android.content.res.Resources

fun Int.dpToPx(): Float {
    return (this * Resources.getSystem().displayMetrics.density)
}

fun Int.pxToDp(): Float {
    return (this / Resources.getSystem().displayMetrics.density)
}

fun Int.spToPx(): Float {
    return (this * Resources.getSystem().displayMetrics.scaledDensity)
}