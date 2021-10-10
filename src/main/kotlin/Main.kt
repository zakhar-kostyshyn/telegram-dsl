import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import models.MethodWrapper
import models.TelegramMethod
import org.koin.java.KoinJavaComponent
import Routes.clientRoutes
import Routes.telegramBotRoutes
import services.code.BasicLexer
import services.code.BasicParser
import services.code.CodeService
import services.eventHandlers.CommandEventHandler
import services.eventHandlers.MessageEventHandler
import services.methods.MethodType
import services.methods.SendService
import services.methods.SendServiceImpl
import services.resolvers.ResolverServiceImpl

val sendService by KoinJavaComponent.inject<SendService>(SendService::class.java)

fun main() {

    val resolver = ResolverServiceImpl(
        messageEventHandler = MessageEventHandler(
            codeService = CodeService(
                lexer = BasicLexer(),
                parser = BasicParser()
            ),
            sendServiceImpl =  SendServiceImpl()
        ),
        commandEventHandler = CommandEventHandler()
    )

    embeddedServer(Netty, port = 8443, host = "127.0.0.1") {
        install(ContentNegotiation) {
            gson()
        }
        routing {
            clientRoutes()
            telegramBotRoutes(resolver)
        }
    }.start()

    sendService.sendMessage(
        MethodWrapper(
            methodType = MethodType.SetWebhook,
            telegramMethodModel = TelegramMethod.SetWebhookMethod(
                url = "https://1317-62-122-200-234.ngrok.io"
            )
        )
    )

}


