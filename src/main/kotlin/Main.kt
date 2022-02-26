import Routes.clientRoutes
import Routes.telegramBotRoutes
import Routes.test
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import models.MethodWrapper
import models.TelegramMethod
import services.code.BasicLexer
import services.code.BasicParser
import services.code.CodeService
import services.eventHandlers.CommandEventHandler
import services.eventHandlers.MessageEventHandler
import services.methods.MethodType
import services.methods.SendServiceImpl
import services.resolvers.ResolverServiceImpl

fun main() {

    // ha ha ha ha ha ha ha ha ha ha ha ha ha ha ha ha (welcome to the club buddy)
    val codeService = CodeService(
        lexer = BasicLexer(),
        parser = BasicParser()
    )

    val sendServiceImpl = SendServiceImpl()

    val resolver = ResolverServiceImpl(
        messageEventHandler = MessageEventHandler(
            codeService = codeService,
            sendServiceImpl = sendServiceImpl
        ),
        commandEventHandler = CommandEventHandler()
    )

    embeddedServer(Netty, port = 8443, host = "127.0.0.1") {
        install(ContentNegotiation) {
            gson()
        }
        routing {
            clientRoutes(codeService)
            telegramBotRoutes(resolver)
            test()
        }
    }.start()

    val response = sendServiceImpl.sendMessage(
        MethodWrapper(
            methodType = MethodType.SetWebhook,
            telegramMethodModel = TelegramMethod.SetWebhookMethod(
                url = "https://a53f-93-175-200-24.ngrok.io"
            )
        )
    )

    println(response)

}


