package config

import io.ktor.application.*
import io.ktor.features.*
import org.koin.dsl.module
import org.koin.ktor.ext.Koin

fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)
    install(Koin) {
        modules(dslCodeProcessingModule)
        modules(actionsForBotModule)
        modules(botWebhooksHandlersModule)
        modules(coreModule)
    }
}


val dslCodeProcessingModule = module {
    // Declare my dependencies
}

val actionsForBotModule = module {
    // Declare my dependencies
}

val botWebhooksHandlersModule = module {
    // Declare my dependencies
}

val coreModule = module {
    // Declare my dependencies
}
