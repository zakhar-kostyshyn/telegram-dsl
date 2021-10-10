fun Any.prettyPrint(whatToDoWithResult: ((String) -> String)? = null): String {

    var indentLevel = 0
    val indentWidth = 4

    fun padding() = "".padStart(indentLevel * indentWidth)

    val toString = toString()

    val stringBuilder = StringBuilder(toString.length)

    var i = 0
    while (i < toString.length) {
        when (val char = toString[i]) {
            '(', '[', '{' -> {
                indentLevel++
                stringBuilder.appendLine(char).append(padding())
            }
            ')', ']', '}' -> {
                indentLevel--
                stringBuilder.appendLine().append(padding()).append(char)
            }
            ',' -> {
                stringBuilder.appendLine(char).append(padding())
                // ignore space after comma as we have added a newline
                val nextChar = toString.getOrElse(i + 1) { char }
                if (nextChar == ' ') i++
            }
            else -> {
                stringBuilder.append(char)
            }
        }
        i++
    }

    val result = stringBuilder.toString()
    return whatToDoWithResult?.invoke(result) ?: result

}


fun <T> MutableList<T>.divide(predicate: (T) -> Boolean): MutableList<MutableList<T>> {

    val highList = mutableListOf<MutableList<T>>()
    var lowList = mutableListOf<T>()

    for (element in this) {
        if (predicate.invoke(element)) {
            highList.add(lowList)
            lowList = mutableListOf()
        } else {
            lowList += element
        }
    }

    highList.add(lowList)

    return highList
}
