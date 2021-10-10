package services.methods

import config.client
import models.TelegramMethod
import models.MethodWrapper
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

interface SendService {
    fun sendMessage(methodWrapper: MethodWrapper<TelegramMethod>): HttpResponse
}

class SendServiceImpl : SendService {

    private val token = "2005666774:AAFOjEOGg_7Q3RGpDHwHPNGrkh25soxz8C8"
    private val client: HttpClient = client()

    override fun sendMessage(methodWrapper: MethodWrapper<TelegramMethod>) = runBlocking {
        client.post("https://api.telegram.org/bot$token/${methodWrapper.methodType}") {
            contentType(ContentType.Application.Json)
            body = methodWrapper.telegramRequest
        } as HttpResponse
    }

}
