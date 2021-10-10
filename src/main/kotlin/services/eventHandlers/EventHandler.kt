package services.eventHandlers

import models.CommandEvent
import models.Event
import models.MessageEvent

interface EventHandler<E : Event> {
    fun handle(event: E)
}

class MessageEventHandler : EventHandler<MessageEvent> {

    override fun handle(event: MessageEvent) {
        println("Handling Message Event")
    }

}

class CommandEventHandler : EventHandler<CommandEvent> {

    override fun handle(event: CommandEvent) {
        println("Handling Command Event")
    }

}
