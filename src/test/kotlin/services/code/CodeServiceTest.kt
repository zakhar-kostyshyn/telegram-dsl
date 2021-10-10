package services.code

import com.nhaarman.mockitokotlin2.mock
import models.DslCode
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

internal class CodeServiceTest {

    @Test
    fun getDslCodeTest() {
        val code = """
            
            function test {
                triggeredBy: update.message - any;
                action: send(update.message.text);
            }

        """.trimIndent()
        val lexer: Lexer = mock()
        val parser: Parser = mock()
        val codeService = CodeService(lexer, parser)

        val result = codeService.getDslCode()


        assertThat(result, CoreMatchers.`is`(DslCode(code)))
    }


}
