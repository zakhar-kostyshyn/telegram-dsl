package config

import services.methods.SendService
import services.methods.SendServiceImpl
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.dsl.single
import services.code.BasicLexer
import services.code.BasicParser
import services.code.Lexer
import services.code.Parser
import services.eventHandlers.CommandEventHandler
import services.eventHandlers.MessageEventHandler
import services.resolvers.Resolver
import services.resolvers.ResolverServiceImpl

val dslCodeProcessingModule = module {
    single<BasicLexer>() bind Lexer::class
    single<BasicParser>() bind Parser::class
}

val actionsForBotModule = module {
    single<SendServiceImpl>() bind SendService::class
    single<ResolverServiceImpl>() bind Resolver::class
    single { MessageEventHandler() }
    single { CommandEventHandler() }
}

val botWebhooksHandlersModule = module {
    // Declare my dependencies
}

val coreModule = module {
    // Declare my dependencies
}
