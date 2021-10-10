package services.code

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import models.Chat
import models.Message
import models.Update
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ResolveReferenceTest {

    @Test
    fun `must resolve reference update_message`() {

        val basicParser = BasicParser()
        val message = Message(message_id = 1L, date = 1L, chat = Chat(id = 1L, type = "test-type"))
        val update = mock<Update> {
            whenever(it.message).thenReturn(message)
        }
        val nodes = listOf(
            Node(nodeType = NodeType.Reference, value = "update"),
            Node(nodeType = NodeType.Reference, value = "message")
        )

        val result = basicParser.getTelegramModelClassByReferences(update, nodes)

        assertThat(result, `is`(Message::class))

    }

    @Test
    fun `must resolve reference update`() {

        val basicParser = BasicParser()
        val message = Message(message_id = 1L, date = 1L, chat = Chat(id = 1L, type = "test-type"), text = "test-text")
        val update = mock<Update> {
            whenever(it.message).thenReturn(message)
        }
        val nodes = listOf(
            Node(nodeType = NodeType.Reference, value = "update"),
        )

        val result = basicParser.getTelegramModelClassByReferences(update, nodes)

        assertThat(result, `is`(Update::class))

    }

    @Test
    fun `DESTRUCTIVE must throw exception when nodes has one non Reference`() {
        val basicParser = BasicParser()
        val update = mock<Update>()
        val nodes = listOf(
            Node(nodeType = NodeType.Reference, value = "update"),
            Node(nodeType = NodeType.Reference, value = "message"),
            Node(nodeType = NodeType.Function, value = "destructive")
        )

        val exception = assertThrows<RuntimeException> {
            basicParser.getTelegramModelClassByReferences(update, nodes)
        }

        assertEquals("nodes are not reference", exception.message)
    }

    @Test
    fun `DESTRUCTIVE must return update when nodes are empty`() {
        val basicParser = BasicParser()
        val update = mock<Update>()
        val nodes = emptyList<Node>()

        val result = basicParser.getTelegramModelClassByReferences(update, nodes)

        assertThat(result, `is`(Update::class))
    }

}
