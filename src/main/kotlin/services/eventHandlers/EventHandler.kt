package services.eventHandlers

import models.*
import models.TelegramMethod.SendMessageMethod
import services.code.CodeService
import services.code.SemanticFunctionAction
import services.code.SemanticFunctionTrigger
import services.code.SemanticFunctionTriggerModelCondition
import services.methods.MethodType
import services.methods.SendService

interface EventHandler<E : Event> {
    fun handle(event: E, update: Update)
}

class MessageEventHandler(
    private val codeService: CodeService,
    private val sendServiceImpl: SendService,
) : EventHandler<MessageEvent> {

    override fun handle(event: MessageEvent, update: Update) {
        val semanticModel = codeService.codeProcessing(
            codeService.getDslCode()!!,
            update
        )

        semanticModel.semanticFunctions.forEach { function ->

            val functionTrigger: SemanticFunctionTrigger = function
                .semanticFunctionProperties
                .filterIsInstance<SemanticFunctionTrigger>()
                .first { trigger -> trigger.triggerModel == event.eventTelegramModelKClass }

            // TODO replace false return when will work on condition for function triggers
            val isActionUse =
                if (functionTrigger.modelCondition.conditionType == SemanticFunctionTriggerModelCondition.ConditionType.Any) true else false

            if (isActionUse) {
                val functionAction: SemanticFunctionAction = function
                    .semanticFunctionProperties
                    .filterIsInstance<SemanticFunctionAction>()
                    .first()

                functionAction.methods.forEach { method ->
                    val telegramMethod: SendMessageMethod = method.method.constructors
                        .first { it.parameters.size == 2 }
                        .call(
                            update.message!!.chat.id.toString(),
                            method.arguments[0].argument
                        ) as SendMessageMethod
                    sendServiceImpl.sendMessage(
                        MethodWrapper(
                            methodType = MethodType.SendMessage,
                            telegramMethodModel = telegramMethod
                        )
                    )
                }

            }

        }

    }

}

class CommandEventHandler : EventHandler<CommandEvent> {

    override fun handle(event: CommandEvent, update: Update) {
        println("Handling Command Event")
    }

}
