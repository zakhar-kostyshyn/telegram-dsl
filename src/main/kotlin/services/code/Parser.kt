package services.code

import divide
import models.TelegramMethod
import models.TelegramModel
import models.Update
import kotlin.reflect.KClass

data class Node(
    val nodeType: NodeType,
    val value: String? = null,
) {
    var childrenNodes: MutableList<Node> = mutableListOf()
    var parentNode: Node? = null
}

enum class NodeType {
    Start, Function, FunctionTrigger, FunctionAction, Entity, EntityCondition, Method, MethodArgument, Reference
}

val functionName2TelegramMethodKClass = mapOf<String, KClass<out TelegramMethod>>(
    "send" to TelegramMethod.SendMessageMethod::class
)


interface Parser {
    fun parse(tokens: List<Token>, update: Update): SemanticModel
}

class BasicParser : Parser {

    override fun parse(tokens: List<Token>, update: Update): SemanticModel {
        val tree = recursiveParse(tokens as MutableList<Token>)
        return buildSemanticModel(tree, update)
    }

    fun recursiveParse(tokens: MutableList<Token>, parentNode: Node = Node(NodeType.Start)): Node {

        println("Working with tokens: '$tokens'")
        println("Working with parent token: '$parentNode'")

        var i = 0
        while (i < tokens.size) {

            if (tokens[i].type == Keyword.FunctionKeyword) {
                println("\tHandle Function")
                val functionName = tokens[i + 2].value
                val functionNode =
                    Node(nodeType = NodeType.Function, value = functionName).apply { this.parentNode = parentNode }
                val openCurlBraceIndex = i + 3
                val workingTokensSubList =
                    excludedTokenSubList(openCurlBraceIndex, Token(Separator.CloseCurlyBrace, "}"), tokens)
                parentNode.childrenNodes += recursiveParse(workingTokensSubList, functionNode)
                i += tokens.indexOf(Token(Separator.CloseCurlyBrace, "}"))
                continue
            }

            if (tokens[i].type == Keyword.TriggerKeyword) {
                println("\tHandle FunctionTrigger")
                val triggerNode = Node(nodeType = NodeType.FunctionTrigger).apply { this.parentNode = parentNode }
                val colonIndex = i + 1
                val workingTokensSubList =
                    excludedTokenSubList(colonIndex, Token(Separator.Semicolon, ";"), tokens)
                parentNode.childrenNodes += recursiveParse(workingTokensSubList, triggerNode)
                i += tokens.indexOf(Token(Separator.Semicolon, ";"))
                continue
            }

            if (tokens[i].type == Text.AnyText &&
                i == 0 &&
                parentNode.nodeType == NodeType.FunctionTrigger
            ) {
                println("\tHandle Entity")
                val entityNode = Node(nodeType = NodeType.Entity).apply { this.parentNode = parentNode }
                val workingTokensSubList = includedTokenSubList(0, Token(Separator.Dash, "-"), tokens)
                parentNode.childrenNodes += recursiveParse(workingTokensSubList, entityNode)
                i += tokens.indexOf(Token(Separator.Dash, "-"))
                continue
            }

            if (tokens[i].type == Text.AnyText &&
                (i == 0 || tokens[i - 1].type == Separator.Point) &&
                parentNode.nodeType in listOf(NodeType.Entity, NodeType.MethodArgument)
            ) {
                println("\tHandle Reference")
                val referenceNode = Node(nodeType = NodeType.Reference, value = tokens[i].value).apply {
                    this.parentNode = parentNode
                }
                parentNode.childrenNodes += recursiveParse(mutableListOf(), referenceNode)
                i++
                continue
            }

            if (parentNode.nodeType == NodeType.FunctionTrigger &&
                tokens[i].type == Text.AnyText &&
                (i > 0 && tokens[i - 1].type == Separator.Dash)
            ) {
                println("\tHandle EntityState")
                val entityStateNode = Node(nodeType = NodeType.EntityCondition, value = tokens[i].value).apply {
                    this.parentNode = parentNode
                }
                parentNode.childrenNodes += recursiveParse(mutableListOf(), entityStateNode)
                i++
                continue
            }

            if (tokens[i].type == Keyword.ActionKeyword) {
                println("\tHandle FunctionAction")
                val actionNode = Node(nodeType = NodeType.FunctionAction).apply { this.parentNode = parentNode }
                val colonIndex = i + 1
                val workingTokensSubList =
                    excludedTokenSubList(colonIndex, Token(Separator.Semicolon, ";"), tokens)
                parentNode.childrenNodes += recursiveParse(workingTokensSubList, actionNode)
                i += tokens.indexOf(Token(Separator.Semicolon, ";"))
                continue
            }

            if (tokens[i].type == Text.AnyText &&
                parentNode.nodeType == NodeType.FunctionAction &&
                (i >= 0 && tokens[i + 1].type == Separator.OpenBracket)
            ) {
                println("\tHandle Method")
                val methodName = tokens[i].value
                val methodNode =
                    Node(nodeType = NodeType.Method, value = methodName).apply { this.parentNode = parentNode }
                parentNode.childrenNodes += methodNode
                val openBracket = i + 1
                excludedTokenSubList(openBracket, Token(Separator.CloseBracket, ")"), tokens)
                    .divide { it.type == Separator.Coma }
                    .forEach {
                        val methodArgument = Node(NodeType.MethodArgument).apply { this.parentNode = methodNode }
                        methodArgument.childrenNodes += recursiveParse(it, methodArgument)
                        methodNode.childrenNodes += methodArgument
                    }
                i += tokens.indexOf(Token(Separator.CloseBracket, ")"))
                continue
            }

            i++
        }

        return parentNode
    }

    fun excludedTokenSubList(left: Int, search: Token, tokenList: MutableList<Token>): MutableList<Token> {
        var right = -1
        var isRightFound = false
        for (i in tokenList.indices) {
            if (left == i) isRightFound = true
            if (isRightFound && tokenList[i] == search) {
                right = i
                isRightFound = false
            }
        }
        return tokenList.subList(left + 1, right)
    }

    fun includedTokenSubList(left: Int, search: Token, tokenList: MutableList<Token>): MutableList<Token> {
        var right = -1
        var isRightFound = false
        for (i in tokenList.indices) {
            if (left == i) isRightFound = true
            if (isRightFound && tokenList[i] == search) {
                right = i
                isRightFound = false
            }
        }
        return tokenList.subList(left, right)
    }

    fun buildSemanticModel(tree: Node, update: Update): SemanticModel {

        val semanticFunctions: List<SemanticFunction> = tree.childrenNodes.map { functionNode ->
            SemanticFunction(
                name = SemanticFunctionName(functionNode.value!!),
                semanticFunctionProperties = if (functionNode.childrenNodes.isEmpty()) emptyList() else listOf(
                    SemanticFunctionTrigger(
                        triggerModel = getTelegramModelClassByReferences(
                            update = update,
                            nodes = functionNode
                                .childrenNodes.first { node -> node.nodeType == NodeType.FunctionTrigger }
                                .childrenNodes.first { node -> node.nodeType == NodeType.Entity }
                                .childrenNodes
                        ),
                        modelCondition = SemanticFunctionTriggerModelCondition(
                            SemanticFunctionTriggerModelCondition.ConditionType.resolveConditionType(
                                functionNode
                                    .childrenNodes.first { node -> node.nodeType == NodeType.FunctionTrigger }
                                    .childrenNodes.first { node -> node.nodeType == NodeType.EntityCondition }.value!!
                            )
                        )
                    ),
                    SemanticFunctionAction(
                        methods = listOf(
                            SemanticMethod(
                                method = functionName2TelegramMethodKClass[
                                    functionNode
                                        .childrenNodes.first { node -> node.nodeType == NodeType.FunctionAction }
                                        .childrenNodes.first { node -> node.nodeType == NodeType.Method }
                                        .value!!
                                ]!!,
                                arguments = functionNode
                                    .childrenNodes.first { node -> node.nodeType == NodeType.FunctionAction }
                                    .childrenNodes.first { node -> node.nodeType == NodeType.Method }
                                    .childrenNodes
                                    .map { methodArgumentNode -> getTelegramModelByReferences(update, methodArgumentNode.childrenNodes) }
                                    .map { valueFromUpdate ->  SemanticMethodArgument(valueFromUpdate) }
                            )
                        )
                    )
                )
            )
        }

        return SemanticModel(semanticFunctions = semanticFunctions)
    }

    fun getTelegramModelClassByReferences(update: Update, nodes: List<Node>): KClass<out TelegramModel> {
        if (nodes.any { it.nodeType != NodeType.Reference }) throw RuntimeException("nodes are not reference")
        val references = nodes.drop(1)
            .map { "get${it.value!!.replaceFirstChar { firstLetter -> firstLetter.titlecase() }}" }
        var currentModel: TelegramModel = update
        references.forEach { currentModel = invokeGetMethod(currentModel, it) }
        return currentModel::class
    }

    fun getTelegramModelByReferences(update: Update, nodes: List<Node>): String {
        if (nodes.any { it.nodeType != NodeType.Reference }) throw RuntimeException("nodes are not reference")
        val references = nodes.drop(1)
            .map { "get${it.value!!.replaceFirstChar { firstLetter -> firstLetter.titlecase() }}" }
        var currentModel: Any = update
        references.forEach { currentModel = invokeGetMethod(currentModel, it) }
        return currentModel as String
    }

    private fun invokeGetMethod(model: Any, methodName: String): Any =
        model.javaClass.getMethod(methodName).invoke(model)

    private fun invokeGetMethod(model: TelegramModel, methodName: String): TelegramModel =
        model.javaClass.getMethod(methodName).invoke(model) as TelegramModel


}











































