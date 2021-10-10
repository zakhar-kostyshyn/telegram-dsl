package routes

import models.DslCode
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*



fun Route.clientRoutes() {

    post("/code") {
        val dslCode = call.receive<DslCode>()

    }

}
