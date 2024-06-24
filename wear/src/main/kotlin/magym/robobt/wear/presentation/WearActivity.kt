/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package magym.robobt.wear.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import magym.robobt.repository.accelerometer.AccelerometerRepository
import magym.robobt.repository.accelerometer.MotorSpeedMapper
import magym.robobt.repository.accelerometer.model.ControlMotorsData
import magym.robobt.repository.connect.bluetooth.BluetoothRepository
import magym.robobt.repository.connect.bluetooth.model.BluetoothResult
import magym.robobt.wear.R
import magym.robobt.wear.presentation.theme.RoboBtTheme
import org.koin.android.ext.android.inject

class WearActivity : ComponentActivity() {

    // TODO: Add TEA
    private val bluetoothRepository: BluetoothRepository by inject()
    private val accelerometerRepository: AccelerometerRepository by inject()
    private val motorSpeedMapper: MotorSpeedMapper = MotorSpeedMapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            WearApp("Android")
        }

        // TODO: Extract to commons
        connect()
    }

    private fun connect() {
        lifecycleScope.launch {
            bluetoothRepository.connect()
                .filter {
                    println("WearActivity: connect result = $it")
                    it is BluetoothResult.Success
                }
                .flatMapLatest { accelerometerRepository.connect().debounce(100) }
                .map(motorSpeedMapper::map)
                .distinctUntilChanged()
                .map(::send)
                .launchIn(lifecycleScope)
        }
    }

    private fun send(data: ControlMotorsData) {
        val isSucceed = bluetoothRepository.send(data.toConnectData())
        if (!isSucceed) connect()
        println("WearActivity: send data = $data, isSucceed = $isSucceed")
    }

    private fun ControlMotorsData.toConnectData(): String {
        return "m" + leftMotor + "z" + rightMotor + "z"
    }
}

@Composable
fun WearApp(greetingName: String) {
    RoboBtTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()
            Greeting(greetingName = greetingName)
        }
    }
}

@Composable
fun Greeting(greetingName: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = stringResource(R.string.hello_world, greetingName)
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}