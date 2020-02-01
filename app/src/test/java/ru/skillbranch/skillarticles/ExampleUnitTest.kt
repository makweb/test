package ru.skillbranch.skillarticles

import org.junit.Test
import ru.skillbranch.skillarticles.extensions.indexesOf
import kotlin.properties.Delegates

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)

        /*var salary: Float by Delegates.observable(0.0F) { prop, old, new ->
            println("prop: $prop old: $old new: $new")
        }
*/


        var salary: Float by Delegates.vetoable (0F) { prop, old, new ->
            println("prop: $prop old: $old new: $new")
            new >= 70000F
        }

        salary = 100F
        println("get value salary: $salary")
        salary = 80000F
        println("get value salary: $salary")

        println("lorem ipsum sum".indexesOf("sum"))

    }
}
