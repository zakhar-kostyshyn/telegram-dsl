package services.code

import models.DslCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import services.code.Keyword.ActionKeyword
import services.code.Keyword.FunctionKeyword
import services.code.Keyword.TriggerKeyword
import services.code.Separator.CloseBracket
import services.code.Separator.CloseCurlyBrace
import services.code.Separator.Colon
import services.code.Separator.Coma
import services.code.Separator.Dash
import services.code.Separator.OpenBracket
import services.code.Separator.OpenCurlyBrace
import services.code.Separator.Point
import services.code.Separator.Semicolon
import services.code.Separator.Whitespace
import services.code.Text.AnyText

internal class BasicLexerTest {

    @Test
    fun filterCodeTest_0() {
        val basicLexer = BasicLexer()
        val code = "   test test - test    }"
        val filteredCode = basicLexer.filterInputCode(code)
        assertEquals("test test-test}", filteredCode)
    }

    @Test
    fun filterCodeTest_1() {
        val basicLexer = BasicLexer()
        val code =
            """
          
            function test {
                triggered: update.message - any;
                handling: send - message;
                }
            """
        val filteredCode = basicLexer.filterInputCode(code)

        assertEquals("function test{triggered:update.message-any;handling:send-message;}", filteredCode)
    }


    @Test
    fun createListOfTokens_0_1() {
        val basicLexer = BasicLexer()
        val filteredInputCode = "{:;.-}"
        val result = basicLexer.createListOfTokens(filteredInputCode)
        assertEquals(result.size, 6)
        assertEquals(result[0], Token(OpenCurlyBrace, "{"))
        assertEquals(result[1], Token(Colon, ":"))
        assertEquals(result[2], Token(Semicolon, ";"))
        assertEquals(result[3], Token(Point, "."))
        assertEquals(result[4], Token(Dash, "-"))
        assertEquals(result[5], Token(CloseCurlyBrace, "}"))
    }


    @Test
    fun createListOfTokens_0() {
        val basicLexer = BasicLexer()
        val filteredInputCode = "test test2-test3}"
        val result = basicLexer.createListOfTokens(filteredInputCode)
        assertEquals(result.size, 6)
        assertEquals(result[0], Token(AnyText, "test"))
        assertEquals(result[1], Token(Whitespace, " "))
        assertEquals(result[2], Token(AnyText, "test2"))
        assertEquals(result[3], Token(Dash, "-"))
        assertEquals(result[4], Token(AnyText, "test3"))
        assertEquals(result[5], Token(CloseCurlyBrace, "}"))
    }

    @Test
    fun createListOfTokens_1() {
        val basicLexer = BasicLexer()
        val filteredInputCode = "function test{triggeredBy:update.message-any;action:send-message;}"
        val result = basicLexer.createListOfTokens(filteredInputCode)
        assertEquals(result.size, 19)
        assertEquals(result[0], Token(FunctionKeyword, "function"))
        assertEquals(result[1], Token(Whitespace, " "))
        assertEquals(result[2], Token(AnyText, "test"))
        assertEquals(result[3], Token(OpenCurlyBrace, "{"))
        assertEquals(result[4], Token(TriggerKeyword, "triggeredBy"))
        assertEquals(result[5], Token(Colon, ":"))
        assertEquals(result[6], Token(AnyText, "update"))
        assertEquals(result[7], Token(Point, "."))
        assertEquals(result[8], Token(AnyText, "message"))
        assertEquals(result[9], Token(Dash, "-"))
        assertEquals(result[10], Token(AnyText, "any"))
        assertEquals(result[11], Token(Semicolon, ";"))
        assertEquals(result[12], Token(ActionKeyword, "action"))
        assertEquals(result[13], Token(Colon, ":"))
        assertEquals(result[14], Token(AnyText, "send"))
        assertEquals(result[15], Token(Dash, "-"))
        assertEquals(result[16], Token(AnyText, "message"))
        assertEquals(result[17], Token(Semicolon, ";"))
        assertEquals(result[18], Token(CloseCurlyBrace, "}"))
    }


    @Test
    fun lexing_0() {
        val basicLexer = BasicLexer()
        val code = """
           function test {
                triggeredBy: update.message - any;
                action: send - message;
            }
 
        """
        val result = basicLexer.lexing(DslCode(code))
        assertEquals(result.size, 19)
        assertEquals(result[0], Token(FunctionKeyword, "function"))
        assertEquals(result[1], Token(Whitespace, " "))
        assertEquals(result[2], Token(AnyText, "test"))
        assertEquals(result[3], Token(OpenCurlyBrace, "{"))
        assertEquals(result[4], Token(TriggerKeyword, "triggeredBy"))
        assertEquals(result[5], Token(Colon, ":"))
        assertEquals(result[6], Token(AnyText, "update"))
        assertEquals(result[7], Token(Point, "."))
        assertEquals(result[8], Token(AnyText, "message"))
        assertEquals(result[9], Token(Dash, "-"))
        assertEquals(result[10], Token(AnyText, "any"))
        assertEquals(result[11], Token(Semicolon, ";"))
        assertEquals(result[12], Token(ActionKeyword, "action"))
        assertEquals(result[13], Token(Colon, ":"))
        assertEquals(result[14], Token(AnyText, "send"))
        assertEquals(result[15], Token(Dash, "-"))
        assertEquals(result[16], Token(AnyText, "message"))
        assertEquals(result[17], Token(Semicolon, ";"))
        assertEquals(result[18], Token(CloseCurlyBrace, "}"))
    }

    @Test
    fun lexing_1() {
        val basicLexer = BasicLexer()
        val code = """
           function test {
                    triggeredBy: update.message - any;
                    action: send(update.message);
                }
 
        """
        val result = basicLexer.lexing(DslCode(code))
        assertEquals(result.size, 22)
        assertEquals(result[0], Token(FunctionKeyword, "function"))
        assertEquals(result[1], Token(Whitespace, " "))
        assertEquals(result[2], Token(AnyText, "test"))
        assertEquals(result[3], Token(OpenCurlyBrace, "{"))
        assertEquals(result[4], Token(TriggerKeyword, "triggeredBy"))
        assertEquals(result[5], Token(Colon, ":"))
        assertEquals(result[6], Token(AnyText, "update"))
        assertEquals(result[7], Token(Point, "."))
        assertEquals(result[8], Token(AnyText, "message"))
        assertEquals(result[9], Token(Dash, "-"))
        assertEquals(result[10], Token(AnyText, "any"))
        assertEquals(result[11], Token(Semicolon, ";"))
        assertEquals(result[12], Token(ActionKeyword, "action"))
        assertEquals(result[13], Token(Colon, ":"))
        assertEquals(result[14], Token(AnyText, "send"))
        assertEquals(result[15], Token(OpenBracket, "("))
        assertEquals(result[16], Token(AnyText, "update"))
        assertEquals(result[17], Token(Point, "."))
        assertEquals(result[18], Token(AnyText, "message"))
        assertEquals(result[19], Token(CloseBracket, ")"))
        assertEquals(result[20], Token(Semicolon, ";"))
        assertEquals(result[21], Token(CloseCurlyBrace, "}"))
    }

    @Test
    fun lexing_2() {
        val basicLexer = BasicLexer()
        val code = """
           
            function test {
                triggeredBy: update.message - any;
                action: send(update.message.text, update.sticker);
            }

        """
        val result = basicLexer.lexing(DslCode(code))
        assertEquals(result.size, 28)
        assertEquals(result[0], Token(FunctionKeyword, "function"))
        assertEquals(result[1], Token(Whitespace, " "))
        assertEquals(result[2], Token(AnyText, "test"))
        assertEquals(result[3], Token(OpenCurlyBrace, "{"))
        assertEquals(result[4], Token(TriggerKeyword, "triggeredBy"))
        assertEquals(result[5], Token(Colon, ":"))
        assertEquals(result[6], Token(AnyText, "update"))
        assertEquals(result[7], Token(Point, "."))
        assertEquals(result[8], Token(AnyText, "message"))
        assertEquals(result[9], Token(Dash, "-"))
        assertEquals(result[10], Token(AnyText, "any"))
        assertEquals(result[11], Token(Semicolon, ";"))
        assertEquals(result[12], Token(ActionKeyword, "action"))
        assertEquals(result[13], Token(Colon, ":"))
        assertEquals(result[14], Token(AnyText, "send"))
        assertEquals(result[15], Token(OpenBracket, "("))
        assertEquals(result[16], Token(AnyText, "update"))
        assertEquals(result[17], Token(Point, "."))
        assertEquals(result[18], Token(AnyText, "message"))
        assertEquals(result[19], Token(Point, "."))
        assertEquals(result[20], Token(AnyText, "text"))
        assertEquals(result[21], Token(Coma, ","))
        assertEquals(result[22], Token(AnyText, "update"))
        assertEquals(result[23], Token(Point, "."))
        assertEquals(result[24], Token(AnyText, "sticker"))
        assertEquals(result[25], Token(CloseBracket, ")"))
        assertEquals(result[26], Token(Semicolon, ";"))
        assertEquals(result[27], Token(CloseCurlyBrace, "}"))
    }

}
