package magym.robobt.common.android

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SuspendableActivityResultHandler<I, O>(
    private val singleActivityHolder: SingleActivityHolder,
	contract: ActivityResultContract<I, O>,
) {

    private val activity: ComponentActivity
		get() = singleActivityHolder.requireActivity()

	private val resultFlow = MutableSharedFlow<O>()

	private val resultLauncher: ActivityResultLauncher<I> = activity.registerForActivityResult(contract) { result ->
		activity.lifecycleScope.launch {
			resultFlow.emit(result)
		}
	}

    suspend fun awaitResultFor(input: I): O {
		resultLauncher.launch(input)
		return resultFlow.first()
    }
}