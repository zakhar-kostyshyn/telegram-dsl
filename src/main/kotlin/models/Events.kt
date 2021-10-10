package models

import kotlin.reflect.KClass

interface Event {
    val eventTelegramModelKClass: KClass<out TelegramModel>
}

data class MessageEvent(
    override val eventTelegramModelKClass: KClass<out Message>,
    val text: String
) : Event


data class CommandEvent(
    override val eventTelegramModelKClass: KClass<out Message>,
    val command: String
) : Event
