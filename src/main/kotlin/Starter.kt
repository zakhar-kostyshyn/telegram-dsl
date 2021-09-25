//import io.ktor.application.*
//import io.ktor.client.*
//import io.ktor.client.engine.cio.*
//import io.ktor.client.features.json.*
//import io.ktor.client.request.*
//import io.ktor.client.statement.*
//import io.ktor.features.*
//import io.ktor.gson.*
//import io.ktor.http.*
//import io.ktor.request.*
//import io.ktor.response.*
//import io.ktor.routing.*
//import io.ktor.server.engine.*
//import io.ktor.server.netty.*
//import kotlinx.coroutines.runBlocking
//
//fun main() {
//
//
//    val token = "2005666774:AAFOjEOGg_7Q3RGpDHwHPNGrkh25soxz8C8"
//    val client = HttpClient(CIO) {
//        install(JsonFeature) {
//            serializer = GsonSerializer()
//        }
//    }
//
//    runBlocking {
//        val response: HttpResponse = client.post("https://api.telegram.org/bot$token/setWebhook") {
//            contentType(ContentType.Application.Json)
//            body = SetWebhookDTO("https://aaee-62-122-200-234.ngrok.io")
//        }
//        println("response = $response")
//
//        val webhookInfo: Wrapper<WebhookInfo> = client.get("https://api.telegram.org/bot$token/getWebhookInfo")
//        println("webhookInfo = $webhookInfo")
//    }
//
//
//    embeddedServer(Netty, port = 8443, host = "127.0.0.1") {
//        install(ContentNegotiation) {
//            gson()
//        }
//        routing {
//            post("/") {
//                val update = call.receive<Update>()
//                println("customer = $update")
//                call.respondText("Customer stored correctly", status = HttpStatusCode.Created)
//            }
//        }
//    }.start(wait = true)
//
//}
//
//data class Customer(
//    val name: String,
//    val age: Int
//)
