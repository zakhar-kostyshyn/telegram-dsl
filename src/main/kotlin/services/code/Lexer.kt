package services.code

import models.DslCode

interface Block

enum class Text : Block {
    AnyText
}

enum class Separator(
    val recognizer: String
) : Block {
    Whitespace("\\s"),
    OpenCurlyBrace("\\{"),
    CloseCurlyBrace("}"),
    Point("\\."),
    Coma(","),
    Dash("-"),
    Semicolon(";"),
    Colon(":"),
    OpenBracket("\\("),
    CloseBracket("\\)")
}

enum class  Keyword(
    val recognizer: String
) : Block {
    FunctionKeyword("function"),
    TriggerKeyword("triggeredBy"),
    ActionKeyword("action"),
}

data class Token(
    val type: Block,
    val value: String
) {
    override fun toString(): String {
        return "$type"
    }
}

interface Lexer {
    fun lexing(dslCode: DslCode): List<Token>
}

class BasicLexer : Lexer {


    override fun lexing(dslCode: DslCode): List<Token> {
        val filteredInputCode = filterInputCode(dslCode.code)
        return createListOfTokens(filteredInputCode)
    }

    fun filterInputCode(code: String) = code.also { println("Start filtering code: '$it'") }
        .trim().also { println("\tCode after trim: '$it'") }
        .replace("\\n".toRegex(), "").also { println("\tCode after new line replace: '$it'") }
        .replace("\\t".toRegex(), "").also { println("\tCode after tab replace: '$it'") }
        .replace("([^\\w\\d])\\s+([\\w\\d])".toRegex(), "$1$2")
        .also { println("\tCode after symbol-word replace: '$it'") }
        .replace("([\\w\\d])\\s+([^\\w\\d])".toRegex(), "$1$2")
        .also { println("\tCode after word-symbol replace: '$it'") }
        .replace("([^\\w\\d])\\s+([^\\w\\d])".toRegex(), "$1$2")
        .also { println("\tCode after symbol-symbol replace: '$it'") }

    fun createListOfTokens(filteredInputCode: String): List<Token> {

        var pattern = ""
        val tokenList = mutableListOf<Token>()

        println("Start token creating of filtered code: '$filteredInputCode'")

        for (letter in filteredInputCode) {
            println("\tWorking letter: '$letter'")
            println("\tPatten remains: '$pattern'")

            val isSeparator = Separator.values().firstOrNull { it.recognizer.toRegex().matches(letter.toString()) } != null
            if (isSeparator) {

                println("\tSeparator '$letter' has been found")

                if (pattern.isNotEmpty()) {
                    println("\t\tPassed pattern of letters '$pattern' wrapped in token '${Text.AnyText}'")
                    tokenList += Token(Text.AnyText, pattern)
                    pattern = ""
                }

                for (tokenType in Separator.values()) {
                    if (letter.toString().matches(tokenType.recognizer.toRegex())) {
                        println("Found separator '$letter' wrapped in token '$tokenType'")
                        tokenList += Token(tokenType, letter.toString())
                    }
                }


            } else {

                println("\tNon-separator '$letter' has been found")

                pattern += letter
                for (tokenType in Keyword.values()) {
                    if (pattern.matches(tokenType.recognizer.toRegex())) {
                        println("\t\tFound word '$pattern' wrapped in token '$tokenType'")
                        tokenList += Token(tokenType, pattern)
                        pattern = ""
                    }
                }

            }

        }

        return tokenList
    }

}


