import kotlinx.serialization.*
import java.io.File

@Serializable
data class SetWebhookDTO(
    val url: String,
    val certificate: File? = null,
    val ip_address: String = "",
    val max_connections: Int = 0,
    val allowed_updates: List<String> = emptyList(),
    val drop_pending_updates: Boolean = false,
)

data class Wrapper<T> (
    val ok: Boolean,
    val result: T
)

data class WebhookInfo(
    val url: String,
    val has_custom_certificate: Boolean,
    val pending_update_count: Int,
    val ip_address: String?,
    val last_error_date: Int?,
    val last_error_message: String?,
    val max_connections: Int?,
    val allowed_updates: List<String>?
)

data class Update(
    val update_id: Int,
    val message: Message?,
    val edited_message: Message?,
    val channel_post: Message?,
    val edited_channel_post: Message?,
    val inline_query: InlineQuery?,
//    val chosen_inline_result: String?,
//    val callback_query: String?,
//    val shipping_query: String?,
//    val pre_checkout_query: String?,
//    val poll: String?,
//    val poll_answer: String?,
//    val my_chat_member: String?,
//    val chat_member: String?,
)

data class Message(
    val message_id: Int
//    val from: Int,
//    val sender_chat: Int,
//    val date: Int,
//    val chat: Int,
//    val forward_from: Int,
//    val forward_from_chat: Int,
//    val forward_from_message_id: Int,
//    val forward_signature: Int,
//    val forward_sender_name: Int,
//    val forward_date: Int,
//    val reply_to_message: Int,
//    val via_bot: Int,
//    val edit_date: Int,
//    val media_group_id: Int,
//    val author_signature: Int,
//    val text: Int,
//    val entities: Int,
//    val animation: Int,
//    val audio: Int,
//    val document: Int,
//    val photo: Int,
//    val sticker	: Int,
//    val video: Int,
//    val video_note: Int,
//    val voice: Int,
//    val caption: Int,
//    val video: Int,
//    val video: Int,
//    val video: Int,
//    val video: Int,
//    val video: Int,
)

data class InlineQuery(
    val id: String,
    val from: User,
    val query: String,
    val offset: String,
    val chat_type: String?,
    val location: Location?,
)

data class User(
    val id: Int,
    val is_bot: Boolean,
    val first_name: String,
    val last_name: String?,
    val username: String?,
    val language_code: String?,
    val can_join_groups: String?,
    val can_read_all_group_messages: String?,
    val supports_inline_queries: String?,
)

data class Location(
    val longitude: Float,
    val latitude: Float,
    val horizontal_accuracy: Float?,
    val live_period: Int?,
    val heading: Int?,
    val proximity_alert_radius: Int?,
)
