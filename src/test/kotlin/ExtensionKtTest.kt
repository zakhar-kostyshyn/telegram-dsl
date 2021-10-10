import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.junit.jupiter.api.Test

internal class ExtensionKtTest {

    @Test
    fun divideTest() {

        val list = mutableListOf("1", "2", "3", ",", "4", "5", "6", ",", "7", "8")
        val dividedList = list.divide { it == "," }
        assertThat(
            dividedList,
            contains(
                mutableListOf("1", "2", "3"),
                mutableListOf("4", "5", "6"),
                mutableListOf("7", "8")
            )
        )
    }


}
