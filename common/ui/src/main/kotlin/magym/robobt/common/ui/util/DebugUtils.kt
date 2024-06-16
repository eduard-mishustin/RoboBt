package magym.robobt.common.ui.util

import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

fun Context.isDebug(): Boolean {
    return applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
}

@Composable
fun isDebug(): Boolean {
    val context = LocalContext.current
    return remember { context.isDebug() }
}