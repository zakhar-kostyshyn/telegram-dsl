package services.code

import config.dslCodeProcessingModule
import models.Chat
import models.DslCode
import models.Message
import models.Update
import org.junit.Rule
import org.junit.jupiter.api.Test
import org.koin.java.KoinJavaComponent.inject
import org.koin.test.KoinTest

internal class CodeServiceTest : KoinTest {

    val lexer by inject<Lexer>(Lexer::class.java)
    val parser by inject<Parser>(Parser::class.java)
//
//    @get:Rule
//    val koinTestRule = KoinTestRule.create {
//        modules(dslCodeProcessingModule)
//    }

    @Test
    fun codeProcessingTest() {

        val codeService = CodeService()

        val dslCode = DslCode(
            """
                
                function test {
                    triggeredBy: update.message - any;
                    action: send(update.message.text);
                }


            """
        )
        val update = Update(
            update_id = 1L,
            message = Message(
                message_id = 1L,
                chat = Chat(
                    id = 1L,
                    type = "test-type"
                ),
                text = "test-message",
                date = 1L
            )
        )

        val codeProcessing: SemanticModel = codeService.codeProcessing(dslCode, update)

    }

}

