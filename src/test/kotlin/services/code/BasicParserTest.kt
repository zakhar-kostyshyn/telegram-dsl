package services.code

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.empty
import org.junit.jupiter.api.Test

internal class BasicParserTest {

    @Test
    fun excludedTokenSubList_1() {
        val basicParser = BasicParser()
        val listOfTokens = mutableListOf(
            Token(Keyword.FunctionKeyword, "function"),
            Token(Separator.Whitespace, " "),
            Token(Text.AnyText, "test"),
            Token(Separator.OpenCurlyBrace, "{"),
            Token(Keyword.TriggerKeyword, "triggeredBy"),
            Token(Separator.Colon, ":"),
            Token(Text.AnyText, "update"),
            Token(Separator.Point, "."),
            Token(Text.AnyText, "message"),
            Token(Separator.Dash, "-"),
            Token(Text.AnyText, "any"),
            Token(Separator.Semicolon, ";"),
            Token(Separator.CloseCurlyBrace, "}")
        )

        val result = basicParser.excludedTokenSubList(3, Token(Separator.CloseCurlyBrace, "}"), listOfTokens)

        assertThat(result, contains(
            Token(Keyword.TriggerKeyword, "triggeredBy"),
            Token(Separator.Colon, ":"),
            Token(Text.AnyText, "update"),
            Token(Separator.Point, "."),
            Token(Text.AnyText, "message"),
            Token(Separator.Dash, "-"),
            Token(Text.AnyText, "any"),
            Token(Separator.Semicolon, ";")
        ))

    }

    @Test
    fun excludedTokenSubList_2() {
        val basicParser = BasicParser()
        val listOfTokens = mutableListOf(
            Token(Keyword.TriggerKeyword, "triggeredBy"),
            Token(Separator.Colon, ":"),
            Token(Text.AnyText, "update"),
            Token(Separator.Point, "."),
            Token(Text.AnyText, "message"),
            Token(Separator.Dash, "-"),
            Token(Text.AnyText, "any"),
            Token(Separator.Semicolon, ";"),
        )

        val result = basicParser.excludedTokenSubList(1, Token(Separator.Semicolon, ";"), listOfTokens)

        assertThat(result, contains(
            Token(Text.AnyText, "update"),
            Token(Separator.Point, "."),
            Token(Text.AnyText, "message"),
            Token(Separator.Dash, "-"),
            Token(Text.AnyText, "any")
        ))
    }

    @Test
    fun excludedTokenSubList_3() {
        val basicParser = BasicParser()
        val listOfTokens = mutableListOf(
            Token(Keyword.FunctionKeyword, "function"),
            Token(Separator.Whitespace, " "),
            Token(Text.AnyText, "test"),
            Token(Separator.OpenCurlyBrace, "{"),
            Token(Separator.CloseCurlyBrace, "}")
        )
        val result = basicParser.excludedTokenSubList(3, Token(Separator.CloseCurlyBrace, "}"), listOfTokens)
        assertThat(result, `is`(empty()))
    }

    @Test
    fun recursivePass_0() {
        val basicParser = BasicParser()
        val listOfTokens = mutableListOf(
            Token(Keyword.FunctionKeyword, "function"),
            Token(Separator.Whitespace, " "),
            Token(Text.AnyText, "test"),
            Token(Separator.OpenCurlyBrace, "{"),
            Token(Separator.CloseCurlyBrace, "}")
        )

        val result = basicParser.recursiveParse(listOfTokens)

        val startNode = Node(NodeType.Start)
        val functionNode = Node(NodeType.Function,"test")

        startNode.childrenNodes += functionNode

        assertThat(result, `is`(startNode))
        assertThat(result.childrenNodes[0], `is`(functionNode))
    }

    @Test
    fun recursivePass_0_1() {
        val basicParser = BasicParser()
        val listOfTokens = mutableListOf(
            Token(Keyword.TriggerKeyword, "triggeredBy"),
            Token(Separator.Colon, ":"),
            Token(Text.AnyText, "update"),
            Token(Separator.Dash, "-"),
            Token(Text.AnyText, "any"),
            Token(Separator.Semicolon, ";"),
        )

        val result = basicParser.recursiveParse(listOfTokens)

        val startNode = Node(NodeType.Start)
        val triggerNode = Node(nodeType = NodeType.FunctionTrigger)
        val entityNode = Node(nodeType = NodeType.Entity)
        val reference1Node = Node(nodeType = NodeType.Reference, value = "update")
        val entityStateNode = Node(nodeType = NodeType.EntityCondition, value = "any")

        startNode.childrenNodes += triggerNode
        triggerNode.childrenNodes += entityNode
        triggerNode.childrenNodes += entityStateNode
        entityNode.childrenNodes += reference1Node

        assertThat(result, `is`(startNode))
        assertThat(result.childrenNodes[0], `is`(triggerNode))
        assertThat(result.childrenNodes[0].childrenNodes[0], `is`(entityNode))
        assertThat(result.childrenNodes[0].childrenNodes[1], `is`(entityStateNode))
        assertThat(result.childrenNodes[0].childrenNodes[0].childrenNodes[0], `is`(reference1Node))
    }

    @Test
    fun recursivePass_0_2() {
        val basicParser = BasicParser()
        val listOfTokens = mutableListOf(
            Token(Keyword.TriggerKeyword, "triggeredBy"),
            Token(Separator.Colon, ":"),
            Token(Text.AnyText, "update"),
            Token(Separator.Point, "."),
            Token(Text.AnyText, "message"),
            Token(Separator.Dash, "-"),
            Token(Text.AnyText, "any"),
            Token(Separator.Semicolon, ";"),
        )

        val result = basicParser.recursiveParse(listOfTokens)

        val startNode = Node(NodeType.Start)
        val triggerNode = Node(nodeType = NodeType.FunctionTrigger)
        val entityNode = Node(nodeType = NodeType.Entity)
        val reference1Node = Node(nodeType = NodeType.Reference, value = "update")
        val reference2Node = Node(nodeType = NodeType.Reference, value = "message")
        val entityStateNode = Node(nodeType = NodeType.EntityCondition, value = "any")

        startNode.childrenNodes += triggerNode
        triggerNode.childrenNodes += entityNode
        triggerNode.childrenNodes += entityStateNode
        entityNode.childrenNodes += reference1Node
        entityNode.childrenNodes += reference2Node

        assertThat(result, `is`(startNode))
        assertThat(result.childrenNodes[0], `is`(triggerNode))
        assertThat(result.childrenNodes[0].childrenNodes[0], `is`(entityNode))
        assertThat(result.childrenNodes[0].childrenNodes[1], `is`(entityStateNode))
        assertThat(result.childrenNodes[0].childrenNodes[0].childrenNodes[0], `is`(reference1Node))
        assertThat(result.childrenNodes[0].childrenNodes[0].childrenNodes[1], `is`(reference2Node))
    }

    @Test
    fun recursivePass_1() {
        val basicParser = BasicParser()
        val listOfTokens = mutableListOf(
            Token(Keyword.FunctionKeyword, "function"),
            Token(Separator.Whitespace, " "),
            Token(Text.AnyText, "test"),
            Token(Separator.OpenCurlyBrace, "{"),
            Token(Keyword.TriggerKeyword, "triggeredBy"),
            Token(Separator.Colon, ":"),
            Token(Text.AnyText, "update"),
            Token(Separator.Point, "."),
            Token(Text.AnyText, "message"),
            Token(Separator.Dash, "-"),
            Token(Text.AnyText, "any"),
            Token(Separator.Semicolon, ";"),
            Token(Separator.CloseCurlyBrace, "}")
        )

        val result = basicParser.recursiveParse(listOfTokens)

        val start = Node(NodeType.Start)
        val function = Node(nodeType = NodeType.Function, value = "test")
        val trigger = Node(nodeType = NodeType.FunctionTrigger)
        val entity = Node(nodeType = NodeType.Entity)
        val reference1 = Node(nodeType = NodeType.Reference, value = "update")
        val reference2 = Node(nodeType = NodeType.Reference, value = "message")
        val entityState = Node(nodeType = NodeType.EntityCondition, value = "any")

        start.childrenNodes += function
        function.childrenNodes += trigger
        trigger.childrenNodes += entity
        trigger.childrenNodes += entityState
        entity.childrenNodes += reference1
        entity.childrenNodes += reference2

        assertThat(result, `is`(start))
        assertThat(result.childrenNodes[0], `is`(function))
        assertThat(result.childrenNodes[0].childrenNodes[0], `is`(trigger))
        assertThat(result.childrenNodes[0].childrenNodes[0].childrenNodes[0], `is`(entity))
        assertThat(result.childrenNodes[0].childrenNodes[0].childrenNodes[0].childrenNodes[0], `is`(reference1))
        assertThat(result.childrenNodes[0].childrenNodes[0].childrenNodes[0].childrenNodes[1], `is`(reference2))
        assertThat(result.childrenNodes[0].childrenNodes[0].childrenNodes[1], `is`(entityState))
    }


    @Test
    fun recursivePass_2_1() {
        val basicParser = BasicParser()
        val listOfTokens = mutableListOf(
            Token(Keyword.FunctionKeyword, "function"),
            Token(Separator.Whitespace, " "),
            Token(Text.AnyText, "test"),
            Token(Separator.OpenCurlyBrace, "{"),
            Token(Keyword.ActionKeyword, "action"),
            Token(Separator.Colon, ":"),
            Token(Text.AnyText, "send"),
            Token(Separator.OpenBracket, "("),
            Token(Text.AnyText, "update"),
            Token(Separator.Point, "."),
            Token(Text.AnyText, "message"),
            Token(Separator.Point, "."),
            Token(Text.AnyText, "text"),
            Token(Separator.CloseBracket, ")"),
            Token(Separator.Semicolon, ";"),
            Token(Separator.CloseCurlyBrace, "}")
        )

        val result = basicParser.recursiveParse(listOfTokens)

        val start = Node(NodeType.Start)
        val function = Node(nodeType = NodeType.Function, value = "test")
        val action = Node(nodeType = NodeType.FunctionAction)
        val method = Node(nodeType = NodeType.Method, value = "send")
        val methodArgument = Node(nodeType = NodeType.MethodArgument)
        val reference1 = Node(nodeType = NodeType.Reference, value = "update")
        val reference2 = Node(nodeType = NodeType.Reference, value = "message")
        val reference3 = Node(nodeType = NodeType.Reference, value = "text")

        start.childrenNodes += function
        function.childrenNodes += action
        action.childrenNodes += method
        method.childrenNodes += methodArgument
        methodArgument.childrenNodes += reference1
        methodArgument.childrenNodes += reference2
        methodArgument.childrenNodes += reference3

        assertThat(result, `is`(start))
        assertThat(result.childrenNodes[0], `is`(function))
        assertThat(result.childrenNodes[0].childrenNodes[0], `is`(action))
        assertThat(result.childrenNodes[0].childrenNodes[0].childrenNodes[0], `is`(method))
        assertThat(result.childrenNodes[0].childrenNodes[0].childrenNodes[0].childrenNodes[0], `is`(methodArgument))
        assertThat(result.childrenNodes[0].childrenNodes[0].childrenNodes[0].childrenNodes[0].childrenNodes[0], `is`(reference1))
        assertThat(result.childrenNodes[0].childrenNodes[0].childrenNodes[0].childrenNodes[0].childrenNodes[1], `is`(reference2))
        assertThat(result.childrenNodes[0].childrenNodes[0].childrenNodes[0].childrenNodes[0].childrenNodes[2], `is`(reference3))
    }

    @Test
    fun recursivePass_2() {
        val basicParser = BasicParser()
        val listOfTokens = mutableListOf(
            Token(Keyword.FunctionKeyword, "function"),
            Token(Separator.Whitespace, " "),
            Token(Text.AnyText, "test"),
            Token(Separator.OpenCurlyBrace, "{"),
            Token(Keyword.TriggerKeyword, "triggeredBy"),
            Token(Separator.Colon, ":"),
            Token(Text.AnyText, "update"),
            Token(Separator.Point, "."),
            Token(Text.AnyText, "message"),
            Token(Separator.Dash, "-"),
            Token(Text.AnyText, "any"),
            Token(Separator.Semicolon, ";"),
            Token(Keyword.ActionKeyword, "action"),
            Token(Separator.Colon, ":"),
            Token(Text.AnyText, "send"),
            Token(Separator.OpenBracket, "("),
            Token(Text.AnyText, "update"),
            Token(Separator.Point, "."),
            Token(Text.AnyText, "message"),
            Token(Separator.Point, "."),
            Token(Text.AnyText, "text"),
            Token(Separator.CloseBracket, ")"),
            Token(Separator.Semicolon, ";"),
            Token(Separator.CloseCurlyBrace, "}")
        )

        val result = basicParser.recursiveParse(listOfTokens)

        val start = Node(NodeType.Start)
        val function = Node(nodeType = NodeType.Function, value = "test")
        val trigger = Node(nodeType = NodeType.FunctionTrigger)
        val entity = Node(nodeType = NodeType.Entity)
        val reference1 = Node(nodeType = NodeType.Reference, value = "update")
        val reference2 = Node(nodeType = NodeType.Reference, value = "message")
        val entityState = Node(nodeType = NodeType.EntityCondition, value = "any")
        val action = Node(nodeType = NodeType.FunctionAction)
        val method = Node(nodeType = NodeType.Method, value = "send")
        val reference3 = Node(nodeType = NodeType.Reference, value = "update")
        val reference4 = Node(nodeType = NodeType.Reference, value = "message")
        val reference5 = Node(nodeType = NodeType.Reference, value = "text")

        start.childrenNodes += function
        function.childrenNodes += trigger
        trigger.childrenNodes += entity
        trigger.childrenNodes += entityState
        entity.childrenNodes += reference1
        entity.childrenNodes += reference2
        function.childrenNodes += action
        action.childrenNodes += method
        method.childrenNodes += reference3
        method.childrenNodes += reference4
        method.childrenNodes += reference5

        assertThat(result, `is`(start))
    }

    @Test
    fun recursivePass_2_reverse() {
        val basicParser = BasicParser()
        val listOfTokens = mutableListOf(
            Token(Keyword.FunctionKeyword, "function"),
            Token(Separator.Whitespace, " "),
            Token(Text.AnyText, "test"),
            Token(Separator.OpenCurlyBrace, "{"),
            Token(Keyword.ActionKeyword, "action"),
            Token(Separator.Colon, ":"),
            Token(Text.AnyText, "send"),
            Token(Separator.OpenBracket, "("),
            Token(Text.AnyText, "update"),
            Token(Separator.Point, "."),
            Token(Text.AnyText, "message"),
            Token(Separator.Point, "."),
            Token(Text.AnyText, "text"),
            Token(Separator.CloseBracket, ")"),
            Token(Separator.Semicolon, ";"),
            Token(Keyword.TriggerKeyword, "triggeredBy"),
            Token(Separator.Colon, ":"),
            Token(Text.AnyText, "update"),
            Token(Separator.Point, "."),
            Token(Text.AnyText, "message"),
            Token(Separator.Dash, "-"),
            Token(Text.AnyText, "any"),
            Token(Separator.Semicolon, ";"),
            Token(Separator.CloseCurlyBrace, "}")
        )

        val result = basicParser.recursiveParse(listOfTokens)

        val start = Node(NodeType.Start)
        val function = Node(nodeType = NodeType.Function, value = "test")
        val trigger = Node(nodeType = NodeType.FunctionTrigger)
        val entity = Node(nodeType = NodeType.Entity)
        val reference1 = Node(nodeType = NodeType.Reference, value = "update")
        val reference2 = Node(nodeType = NodeType.Reference, value = "message")
        val entityState = Node(nodeType = NodeType.EntityCondition, value = "any")
        val action = Node(nodeType = NodeType.FunctionAction)
        val method = Node(nodeType = NodeType.Method, value = "send")
        val reference3 = Node(nodeType = NodeType.Reference, value = "update")
        val reference4 = Node(nodeType = NodeType.Reference, value = "message")
        val reference5 = Node(nodeType = NodeType.Reference, value = "text")

        start.childrenNodes += function
        function.childrenNodes += action
        action.childrenNodes += method
        method.childrenNodes += reference3
        method.childrenNodes += reference4
        method.childrenNodes += reference5
        function.childrenNodes += trigger
        trigger.childrenNodes += entity
        trigger.childrenNodes += entityState
        entity.childrenNodes += reference1
        entity.childrenNodes += reference2

        assertThat(result, `is`(start))
    }


    @Test
    fun recursivePass_2_sticker() {
        val basicParser = BasicParser()
        val listOfTokens = mutableListOf(
            Token(Keyword.FunctionKeyword, "function"),
            Token(Separator.Whitespace, " "),
            Token(Text.AnyText, "test"),
            Token(Separator.OpenCurlyBrace, "{"),
            Token(Keyword.TriggerKeyword, "triggeredBy"),
            Token(Separator.Colon, ":"),
            Token(Text.AnyText, "update"),
            Token(Separator.Point, "."),
            Token(Text.AnyText, "message"),
            Token(Separator.Dash, "-"),
            Token(Text.AnyText, "any"),
            Token(Separator.Semicolon, ";"),
            Token(Keyword.ActionKeyword, "action"),
            Token(Separator.Colon, ":"),
            Token(Text.AnyText, "send"),
            Token(Separator.OpenBracket, "("),
            Token(Text.AnyText, "update"),
            Token(Separator.Point, "."),
            Token(Text.AnyText, "message"),
            Token(Separator.Point, "."),
            Token(Text.AnyText, "text"),
            Token(Separator.Coma, ","),
            Token(Text.AnyText, "update"),
            Token(Separator.Point, "."),
            Token(Text.AnyText, "sticker"),
            Token(Separator.CloseBracket, ")"),
            Token(Separator.Semicolon, ";"),
            Token(Separator.CloseCurlyBrace, "}")
        )

        val result = basicParser.recursiveParse(listOfTokens)

        val start = Node(NodeType.Start)
        val function = Node(nodeType = NodeType.Function, value = "test")
        val trigger = Node(nodeType = NodeType.FunctionTrigger)
        val entity = Node(nodeType = NodeType.Entity)
        val reference1 = Node(nodeType = NodeType.Reference, value = "update")
        val reference2 = Node(nodeType = NodeType.Reference, value = "message")
        val entityState = Node(nodeType = NodeType.EntityCondition, value = "any")
        val action = Node(nodeType = NodeType.FunctionAction)
        val method = Node(nodeType = NodeType.Method, value = "send")
        val methodArgument1 = Node(nodeType = NodeType.MethodArgument)
        val reference3 = Node(nodeType = NodeType.Reference, value = "update")
        val reference4 = Node(nodeType = NodeType.Reference, value = "message")
        val reference5 = Node(nodeType = NodeType.Reference, value = "text")
        val methodArgument2 = Node(nodeType = NodeType.MethodArgument)
        val reference6 = Node(nodeType = NodeType.Reference, value = "update")
        val reference7 = Node(nodeType = NodeType.Reference, value = "sticker")

        start.childrenNodes += function
        function.childrenNodes += trigger
        trigger.childrenNodes += entity
        trigger.childrenNodes += entityState
        entity.childrenNodes += reference1
        entity.childrenNodes += reference2
        function.childrenNodes += action
        action.childrenNodes += method
        method.childrenNodes += methodArgument1
        methodArgument1.childrenNodes += reference3
        methodArgument1.childrenNodes += reference4
        methodArgument1.childrenNodes += reference5
        method.childrenNodes += methodArgument2
        methodArgument2.childrenNodes += reference6
        methodArgument2.childrenNodes += reference7

        assertThat(result, `is`(start))
    }


}
