package magym.robobt.web

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * https://dashboard.ngrok.com/get-started/setup/windows
 * ngrok http http://127.0.0.1:8080
 */
fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

val flow = MutableStateFlow("")

fun Application.module() {
    install(WebSockets)

    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }

        post("/") {
            val data = call.queryParameters["data"].orEmpty()
            flow.emit(data)
        }

        webSocket("/connect") {
            try {
                coroutineScope {
                    launch {
                        while (true) {
                            val frame = incoming.receive()
                            if (frame is Frame.Text) {
                                println("Received: ${frame.readText()}")
                            }
                        }
                    }
                    flow.onEach {
                        send(Frame.Text(it))
                    }.launchIn(this)
                }
            } catch (e: Exception) {
                println("WebSocket error: ${e.message}")
            } finally {
                println("WebSocket connection closed.")
            }
        }
    }
}