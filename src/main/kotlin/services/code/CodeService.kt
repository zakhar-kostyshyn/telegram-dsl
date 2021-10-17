package services.code

import models.DslCode
import models.Update
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.nio.file.Files.exists
import java.nio.file.Paths

class CodeService(
    private val lexer: Lexer,
    private val parser: Parser
) {

    private val dslCodePath = "src/main/resources/dsl_code.txt"

    fun codeProcessing(dslCode: DslCode, update: Update): SemanticModel {
        val tokens: List<Token> = lexer.lexing(dslCode)
        return parser.parse(tokens, update)
    }


    fun getDslCode(): DslCode? =
        if (isDslCodeFileExistInResource()) {
            DslCode(
                code = File(dslCodePath).readText()
            )
        } else null


    fun setDslCode(dslCode: DslCode) = if (isDslCodeFileExistInResource()) {
        val bufferedWriter = BufferedWriter(FileWriter(dslCodePath))
        bufferedWriter.use {
            it.write(dslCode.code)
        }
    } else {
        throw Exception("no dsl_code.txt file")
    }

    private fun isDslCodeFileExistInResource(): Boolean =
        exists(Paths.get(dslCodePath))

}
