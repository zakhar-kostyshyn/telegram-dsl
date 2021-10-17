import Logging.logDslCode
import Logging.logUpdate
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.routing.*
import models.DslCode
import models.Update
import services.code.CodeService
import services.resolvers.Resolver

object Routes {

    fun Route.telegramBotRoutes(resolver: Resolver) {
        post("/") {
            val update = call.receive<Update>()
            call.response.status(HttpStatusCode.OK)     // need to say telegram that webhook handled correctly
            logUpdate(call, update)
            resolver.resolve(update)
        }
    }

    fun Route.clientRoutes(codeService: CodeService) {
        post("/code") {
            val dslCode = call.receive<DslCode>()
            logDslCode(call, dslCode)
            codeService.setDslCode(dslCode)
        }
    }
}
