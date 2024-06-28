package magym.robobt.common.android

import androidx.activity.ComponentActivity
import java.lang.ref.WeakReference

class SingleActivityHolder {

    lateinit var activity: WeakReference<ComponentActivity>

    fun getActivity(): ComponentActivity? {
        return activity.get()
    }

    fun requireActivity(): ComponentActivity {
        return activity.get()!!
    }
}