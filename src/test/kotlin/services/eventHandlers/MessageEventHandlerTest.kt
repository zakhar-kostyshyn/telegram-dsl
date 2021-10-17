package services.eventHandlers

import com.nhaarman.mockitokotlin2.*
import models.*
import models.TelegramMethod.SendMessageMethod
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import services.code.*
import services.methods.MethodType
import services.methods.SendServiceImpl

internal class MessageEventHandlerTest {

    private val code = """
           
            function test {
                triggeredBy: update.message - any;
                action: send(update.message.text, update.sticker);
            }

        """

    @Test
    fun handleTest() {

        // setup
        val semanticModel = SemanticModel(semanticFunctions = listOf(
            SemanticFunction(name = SemanticFunctionName("test"), semanticFunctionProperties = listOf(
                SemanticFunctionTrigger(triggerModel = Message::class, modelCondition = SemanticFunctionTriggerModelCondition(SemanticFunctionTriggerModelCondition.ConditionType.Any)),
                SemanticFunctionAction(methods = listOf(SemanticMethod(method = SendMessageMethod::class, arguments = listOf(SemanticMethodArgument(argument = "test-message")))))
        ))))
        val codeService = mock<CodeService>()
        val sendServiceImpl = mock<SendServiceImpl>()
        val messageEventHandler = MessageEventHandler(codeService, sendServiceImpl)
        whenever(codeService.getDslCode()).thenReturn(DslCode(code))
        whenever(codeService.codeProcessing(any(), any())).thenReturn(semanticModel)
        val message = Message(message_id = 1L, date = 1L, chat = Chat(id = 2L, type = "test-type"), text = "test-message")
        val update = mock<Update> {
            whenever(it.message).thenReturn(message)
        }
        val messageEvent = MessageEvent(Message::class, "test-message")

        // act
        messageEventHandler.handle(messageEvent, update)

        // verify
        argumentCaptor<MethodWrapper<SendMessageMethod>> {
            verify(sendServiceImpl).sendMessage(capture())
            assertThat(firstValue.methodType, `is`(MethodType.SendMessage))
            assertThat(firstValue.telegramMethodModel, `is`(SendMessageMethod(
                chat_id = "2",
                text = "test-message"
            )))
        }

    }


}















