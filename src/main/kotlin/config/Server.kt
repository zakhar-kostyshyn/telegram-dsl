package config

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import routes.clientRoutes
import routes.telegramBotRoutes

fun server() {
    embeddedServer(Netty, port = 8443, host = "127.0.0.1") {
        install(ContentNegotiation) { gson() }
        install(Routing)
        routing {
            clientRoutes()
            telegramBotRoutes()
        }
    }.start()
}
