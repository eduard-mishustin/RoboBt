package magym.robobt.web

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.input.key.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.awt.event.KeyEvent

val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

fun main() = application {
    val keyStates = remember {
        mutableStateMapOf<String, Boolean>()
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "RoboBtWeb",
        onKeyEvent = { keyEvent ->
            if (keyEvent.key == Key.F || keyEvent.key == Key.V || keyEvent.key == Key.K || keyEvent.key == Key.M) {
                val keyName = KeyEvent.getKeyText(keyEvent.key.nativeKeyCode)
                when (keyEvent.type) {
                    KeyEventType.KeyDown -> {
                        if (!keyStates.getOrDefault(keyName, false)) {
                            sendKeyToServer(keyName, true)
                            println("KeyDown ${keyEvent.key}")
                            keyStates[keyName] = true
                        }
                    }

                    KeyEventType.KeyUp -> {
                        sendKeyToServer(keyName, false)
                        println("KeyUp ${keyEvent.key}")
                        keyStates[keyName] = false
                    }
                }
                true
            } else {
                // let other handlers receive this event
                false
            }
        }
    ) {
        App()
    }
}

val client = HttpClient(CIO)

private fun sendKeyToServer(keyName: String, boolean: Boolean) {
    scope.launch {
        client.post("http://127.0.0.1:8080/") {
            url {
                parameters.append("key", keyName)
                parameters.append("is_down", boolean.toString())
            }
        }
    }
}