package services.resolvers

import models.*
import org.koin.java.KoinJavaComponent
import services.eventHandlers.CommandEventHandler
import services.eventHandlers.MessageEventHandler

class ResolverServiceImpl : Resolver {

    private val messageEventHandler by KoinJavaComponent.inject<MessageEventHandler>(MessageEventHandler::class.java)
    private val commandEventHandler by KoinJavaComponent.inject<CommandEventHandler>(CommandEventHandler::class.java)

    override fun resolve(update: Update) {
        val message = update.message
        requireNotNull(message) { "Message for this resolver can't be null" }
        val botCommandCondition: Boolean = message.entities?.firstOrNull { it.type == "bot_command" } != null && message.text != null
        val simpleMessageCondition = message.text != null

        if (botCommandCondition)
            return commandEventHandler.handle(CommandEvent(command = message.text!!))

        if (simpleMessageCondition)
            return messageEventHandler.handle(MessageEvent(text = message.text!!))

        throw RuntimeException("Resolver can't handle update without message")
    }

}
