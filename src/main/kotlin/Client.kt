import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*

fun client() = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = GsonSerializer()
    }
}
