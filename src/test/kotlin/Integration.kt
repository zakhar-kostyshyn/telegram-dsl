import models.Chat
import models.Message
import models.Update
import models.User
import org.junit.jupiter.api.Test
import services.code.BasicLexer
import services.code.BasicParser
import services.code.CodeService
import services.eventHandlers.CommandEventHandler
import services.eventHandlers.MessageEventHandler
import services.methods.SendServiceImpl
import services.resolvers.ResolverServiceImpl

internal class Integration {


    @Test
    fun testFullFlow() {

        // setup
        val update = Update(
            update_id = 3,
            message = Message(
                message_id = 2,
                from = User(
                    id = 1471380466,
                    is_bot = false,
                    first_name = "Zakhar",
                    username = "zakhar_kost",
                    language_code = "uk"
                ),
                date = 1634501758,
                chat = Chat(
                    id = 1471380466,
                    type = "private",
                    username = "zakhar_kost",
                    first_name = "Zakhar"
                ),
                text = "test"
            )
        )
        val codeService = CodeService(
            lexer = BasicLexer(),
            parser = BasicParser()
        )
        val sendServiceImpl = SendServiceImpl()
        val resolver = ResolverServiceImpl(
            messageEventHandler = MessageEventHandler(
                codeService = codeService,
                sendServiceImpl = sendServiceImpl
            ),
            commandEventHandler = CommandEventHandler()
        )

        // act
        resolver.resolve(update)
    }

}
