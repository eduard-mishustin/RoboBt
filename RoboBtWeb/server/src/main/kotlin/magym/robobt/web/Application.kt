package magym.robobt.web

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.server.websocket.WebSockets
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * https://dashboard.ngrok.com/get-started/setup/windows
 * ngrok http http://127.0.0.1:8080
 */
fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

val flow = MutableStateFlow("")
val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

fun Application.module() {
    install(WebSockets)

    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }

        post("/") {
            val data = call.queryParameters["data"].orEmpty()
            //            flow.emit(data)
        }

        webSocket("/connect") {
            try {
                coroutineScope {
                    launch {
                        while (true) {
                            val frame = incoming.receive()
                            if (frame is Frame.Text) {
                                println("connect Received: ${frame.readText()}")
                            }
                        }
                    }
                    flow.onEach {
                        println("connect send: ${it}")
                        send(Frame.Text(it))
                    }.launchIn(this)
                }
            } catch (e: Exception) {
                println("WebSocket error: ${e.message}")
            } finally {
                println("WebSocket connection closed.")
            }
        }

        webSocket("/send") {
            try {
                coroutineScope {
                    launch {
                        while (true) {
                            val frame = incoming.receive()
                            if (frame is Frame.Text) {
                                val readText = frame.readText()
                                println("send Received: $readText")
                                flow.emit(readText)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                println("WebSocket error: ${e.message}")
            } finally {
                println("WebSocket connection closed.")
            }
        }
    }

    camConnection()
}