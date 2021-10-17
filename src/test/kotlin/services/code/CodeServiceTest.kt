package services.code

internal class CodeServiceTest {

    private val code = """function test {
    triggeredBy: update.message - any;
    action: send(update.message.text);
}
"""

}
