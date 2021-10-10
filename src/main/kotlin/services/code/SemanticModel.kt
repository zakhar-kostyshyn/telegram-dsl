package services.code

import models.TelegramMethod
import models.TelegramModel
import kotlin.reflect.KClass

data class SemanticModel(val semanticFunctions: List<SemanticFunction> = emptyList())

data class SemanticFunction(val name: SemanticFunctionName, val semanticFunctionProperties: List<SemanticFunctionProperty> = emptyList())
data class SemanticFunctionName(val name: String)

sealed class SemanticFunctionProperty
data class SemanticFunctionTrigger(val triggerModel: KClass<out TelegramModel>, val modelCondition: SemanticFunctionTriggerModelCondition) : SemanticFunctionProperty()
data class SemanticFunctionAction(val methods: List<SemanticMethod>) : SemanticFunctionProperty()

// TODO add predicate object when condition will need
data class SemanticFunctionTriggerModelCondition(val conditionType: ConditionType, val predicate: Any? = null) {
    enum class ConditionType {
        Any, Predicate;
        companion object {
            fun resolveConditionType(value: String): ConditionType = if (value.lowercase() == "any") Any else Predicate
        }
    }
}
data class SemanticMethod(val method: KClass<out TelegramMethod>, val arguments: List<SemanticMethodArgument>)
data class SemanticMethodArgument(val argument: String)
