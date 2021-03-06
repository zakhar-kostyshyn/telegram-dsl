import models.Update
import io.ktor.application.*
import models.DslCode

object Logging {

    fun logUpdate(call: ApplicationCall, update: Update) {
        val prettiedUpdate = update.prettyPrint {
            it.lines()
                .filter { line -> !line.contains(Regex("=null")) }
                .joinToString(separator = "\n")
        }
        call.application.environment.log.info("update = $prettiedUpdate")
    }

    fun logDslCode(call: ApplicationCall, dslCode: DslCode) {
        call.application.environment.log.info("update = ${dslCode.code}")
    }

}
