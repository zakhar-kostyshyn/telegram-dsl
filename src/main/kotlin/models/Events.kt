package models

interface Event

data class MessageEvent(
    val text: String
) : Event


data class CommandEvent(
    val command: String
) : Event
