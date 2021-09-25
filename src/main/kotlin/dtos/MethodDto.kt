package dtos

import methods.MethodType
import java.io.File

data class MethodDtoWrapper<T>(
    val methodType: MethodType,
    val methodDto: T
)

data class SetWebhookDTO(
    val url: String,
    val certificate: File? = null,
    val ip_address: String = "",
    val max_connections: Int = 0,
    val allowed_updates: List<String> = emptyList(),
    val drop_pending_updates: Boolean = false,
)
