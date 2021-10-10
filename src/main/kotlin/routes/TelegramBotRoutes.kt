package routes

import Logging.logUpdate
import models.Update
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.routing.*
import org.koin.java.KoinJavaComponent.inject
import services.resolvers.Resolver

val resolver by inject<Resolver>(Resolver::class.java)

fun Route.telegramBotRoutes() {
    post("/") {
        val update = call.receive<Update>()
        call.response.status(HttpStatusCode.OK)     // need to say telegram that webhook handled correctly
        logUpdate(call, update)
        resolver.resolve(update)
    }
}
