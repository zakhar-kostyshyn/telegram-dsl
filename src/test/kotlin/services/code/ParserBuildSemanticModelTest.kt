package services.code

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import models.Chat
import models.Message
import models.TelegramMethod
import models.Update
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import services.code.NodeType.*

internal class ParserBuildSemanticModelTest {

    @Test
    fun `must create SemanticModel with one empty function with name`() {
        val basicParser = BasicParser()
        val update = mock<Update>()
        val tree = Node(Start).apply {
            childrenNodes = mutableListOf(
                Node(Function, "test")
            )
        }
        val result = basicParser.buildSemanticModel(tree, update)
        assertThat(
            result, `is`(
                SemanticModel(
                    semanticFunctions = listOf(
                        SemanticFunction(name = SemanticFunctionName("test"), semanticFunctionProperties = emptyList())
                    )
                )
            )
        )
    }


    @Test
    fun `must create SemanticModel with one function with name and trigger`() {
        val basicParser = BasicParser()
        val message = Message(message_id = 1L, date = 1L, chat = Chat(id = 1L, type = "test-type"), text = "test-text")
        val update = mock<Update> {
            whenever(it.message).thenReturn(message)
        }
        val tree = Node(Start).apply {
            childrenNodes = mutableListOf(
                Node(Function, "test").apply {
                    childrenNodes = mutableListOf(
                        Node(FunctionTrigger).apply {
                            childrenNodes = mutableListOf(
                                Node(Entity).apply {
                                    childrenNodes = mutableListOf(
                                        Node(Reference, "update"),
                                        Node(Reference, "message")
                                    )
                                },
                                Node(EntityCondition,"any")
                            )
                        }
                    )
                }
            )
        }
        val result = basicParser.buildSemanticModel(tree, update)
        assertThat(
            result, `is`(
                SemanticModel(
                    semanticFunctions = listOf(
                        SemanticFunction(
                            name = SemanticFunctionName("test"),
                            semanticFunctionProperties = listOf(
                                SemanticFunctionTrigger(
                                    triggerModel = Message::class,
                                    modelCondition = SemanticFunctionTriggerModelCondition(SemanticFunctionTriggerModelCondition.ConditionType.Any)
                                )
                            )
                        )
                    )
                )
            )
        )
    }

    @Test
    fun `must create SemanticModel with one function  trigger-entity-entity_condition and action-method `() {
        val basicParser = BasicParser()
        val message = Message(message_id = 1L, date = 1L, chat = Chat(id = 1L, type = "test-type"), text = "test-text")
        val update = mock<Update> {
            whenever(it.message).thenReturn(message)
        }
        val tree = Node(Start).apply {
            childrenNodes = mutableListOf(
                Node(Function, "test").apply {
                    childrenNodes = mutableListOf(
                        Node(FunctionTrigger).apply {
                            childrenNodes = mutableListOf(
                                Node(Entity).apply {
                                    childrenNodes = mutableListOf(
                                        Node(Reference, "update"),
                                        Node(Reference, "message")
                                    )
                                },
                                Node(EntityCondition, "any")
                            )
                        },
                        Node(FunctionAction).apply {
                            childrenNodes = mutableListOf(
                                Node(Method, "send").apply {
                                    childrenNodes = mutableListOf(
                                        Node(MethodArgument).apply {
                                            childrenNodes = mutableListOf(
                                                Node(Reference, "update"),
                                                Node(Reference, "message"),
                                                Node(Reference, "text")
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    )
                }
            )
        }

        val result = basicParser.buildSemanticModel(tree, update)

        assertThat(
            result, `is`(
                SemanticModel(
                    semanticFunctions = listOf(
                        SemanticFunction(
                            name = SemanticFunctionName("test"),
                            semanticFunctionProperties = listOf(
                                SemanticFunctionTrigger(
                                    triggerModel = Message::class,
                                    modelCondition = SemanticFunctionTriggerModelCondition(SemanticFunctionTriggerModelCondition.ConditionType.Any)
                                ),
                                SemanticFunctionAction(
                                    methods = listOf(
                                        SemanticMethod(
                                            method = TelegramMethod.SendMessageMethod::class,
                                            arguments = listOf(
                                                SemanticMethodArgument("test-text")
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

    }

}
