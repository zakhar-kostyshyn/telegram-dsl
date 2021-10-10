import config.actionsForBotModule
import config.botWebhooksHandlersModule
import config.coreModule
import config.dslCodeProcessingModule
import models.MethodWrapper
import models.TelegramMethod
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import services.methods.MethodType
import services.methods.SendService
import org.koin.core.context.GlobalContext.startKoin
import org.koin.java.KoinJavaComponent
import routes.clientRoutes
import routes.telegramBotRoutes

val sendService by KoinJavaComponent.inject<SendService>(SendService::class.java)

fun main() {

    embeddedServer(Netty, port = 8443, host = "127.0.0.1") {
        install(ContentNegotiation) {
            gson()
        }
        routing {
            clientRoutes()
            telegramBotRoutes()
        }
    }.start()

    startKoin {
        modules(dslCodeProcessingModule)
        modules(actionsForBotModule)
        modules(botWebhooksHandlersModule)
        modules(coreModule)
    }

    sendService.sendMessage(
        MethodWrapper(
            methodType = MethodType.SetWebhook,
            telegramRequest = TelegramMethod.SetWebhookMethod(
                url = "https://1317-62-122-200-234.ngrok.io"
            )
        )
    )

}


