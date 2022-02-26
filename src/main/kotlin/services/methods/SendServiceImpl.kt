package services.methods

import client
import models.TelegramMethod
import models.MethodWrapper
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

interface SendService {
    fun sendMessage(methodWrapper: MethodWrapper<out TelegramMethod>): HttpResponse
}

class SendServiceImpl : SendService {

    private val token = "2086238171:AAGOSqJCtq28xL4Fi7ffH1cVZY3UYew2GKY"
    private val client: HttpClient = client()

    override fun sendMessage(methodWrapper: MethodWrapper<out TelegramMethod>) = runBlocking {
        client.post("https://api.telegram.org/bot$token/${methodWrapper.methodType.methodType}") {
            contentType(ContentType.Application.Json)
            body = methodWrapper.telegramMethodModel
        } as HttpResponse
    }

}
