package services.code

import models.DslCode
import models.Update

class CodeService(
    private val lexer: Lexer,
    private val parser: Parser
) {

    fun codeProcessing(dslCode: DslCode, update: Update): SemanticModel {
        val tokens: List<Token> = lexer.lexing(dslCode)
        return parser.parse(tokens, update)
    }

    fun getDslCode(): DslCode {
        val code = this.javaClass::class.java.getResource("/dsl_code.txt").readText()
        return DslCode(code)
    }

}
