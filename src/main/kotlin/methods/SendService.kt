package methods

import config.client
import dtos.MethodDtoWrapper
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

class SendService<T> {

    private val token = "2005666774:AAFOjEOGg_7Q3RGpDHwHPNGrkh25soxz8C8"
    private val client: HttpClient = client()

    // TODO write response for sendMessage
    fun sendMessage(methodDtoWrapper: MethodDtoWrapper<T>) = runBlocking {
        client.post("https://api.telegram.org/bot$token/${methodDtoWrapper.methodType}") {
            contentType(ContentType.Application.Json)
            body = methodDtoWrapper.methodDto!!
        } as HttpResponse
    }

}
