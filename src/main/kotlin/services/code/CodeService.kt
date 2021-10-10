package services.code

import models.DslCode
import models.Update
import org.koin.java.KoinJavaComponent

val lexer by KoinJavaComponent.inject<Lexer>(Lexer::class.java)
val parser by KoinJavaComponent.inject<Parser>(Parser::class.java)

class CodeService {

    fun codeProcessing(dslCode: DslCode, update: Update): SemanticModel {
        val tokens: List<Token> = lexer.lexing(dslCode)
        return parser.parse(tokens, update)
    }

}
