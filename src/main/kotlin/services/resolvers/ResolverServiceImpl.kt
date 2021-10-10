package services.resolvers

import models.*
import org.koin.java.KoinJavaComponent
import services.eventHandlers.CommandEventHandler
import services.eventHandlers.MessageEventHandler

class ResolverServiceImpl(
    private val messageEventHandler: MessageEventHandler,
    private val commandEventHandler: CommandEventHandler
) : Resolver {

    override fun resolve(update: Update) {
        val message: Message? = update.message
        requireNotNull(message) { "Message for this resolver can't be null" }
        val botCommandCondition: Boolean = message.entities?.firstOrNull { it.type == "bot_command" } != null && message.text != null
        val simpleMessageCondition = message.text != null

        if (botCommandCondition)
            return commandEventHandler.handle(
                CommandEvent(
                    command = message.text!!,
                    eventTelegramModelKClass = message::class
                ),
                update = update
            )

        if (simpleMessageCondition)
            return messageEventHandler.handle(
                MessageEvent(
                    text = message.text!!,
                    eventTelegramModelKClass = message::class
                ),
                update = update
            )

        throw RuntimeException("Resolver can't handle update without message")
    }

}
