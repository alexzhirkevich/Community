package github.alexzhirkevich.community

import android.content.Context
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.typeOf

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @ExperimentalStdlibApi
    @Test(timeout = 200)
    fun useAppContext() {

        ReflectionTester::class.memberFunctions.find {
            it.annotations.any { it is A && it.i == 32 } &&
                    it.returnType == typeOf<Int>() &&
                    it.parameters.size == 2 &&
                    it.parameters[1].type == typeOf<String>()
        }!!.let {
            println(it.call(ReflectionTester,""))
        }

    }

    @Test(timeout = 5)
    fun useAppContext2() {
        println(123)
    }
}

annotation class A(val i: Int)

object ReflectionTester{

    @A(1)
    fun a(s : String) : Int = 1

    @A(2)
    fun aa(s : String) : Int = 1

    @A(3)
    fun aaa(s : String) : Int = 1

    @A(4)
    fun aaaa(s : String) : Int = 1

    @A(5)
    fun aaaaa(s : String) : Int = 1

    @A(6)
    fun aaaaaa(s : String) : Int = 1

    @A(7)
    fun f(s : String) : Int = 1

    @A(8)
    fun ff(s : String) : Int = 1

    @A(9)
    fun fff(s : String) : Int = 1

    @A(10)
    fun ffff(s : String) : Int = 1

    @A(11)
    fun ffasdfsadf(s : String) : Int = 1

    @A(12)
    fun fasdfsadf(s : String) : Int = 1

    @A(13)
    fun asfgfsdgdf(s : String) : Int = 1

    @A(14)
    fun fasdfasdfasdf(s : String) : Int = 1

    @A(15)
    fun ffdsasfd(s : String) : Int = 1

    @A(16)
    fun fgjfdsljgn(s : String) : Int = 1

    @A(17)
    fun fsdfkghsdjfgk(s : String) : Int = 1

    @A(18)
    fun fsdfgjsdjfkgkjsd(s : String) : Int = 1

    @A(19)
    fun ffasfdsgsdfg(s : String) : Int = 1

    @A(20)
    fun fsdfkjgskdjfgkjsd(s : String) : Int = 1

    @A(21)
    fun sdfdsdsf(s : String) : Int = 1

    @A(22)
    fun sdfg(s : String) : Int = 1

    @A(23)
    fun osfg(s : String) : Int = 1

    @A(24)
    fun sdfgkdsjfg(s : String) : Int = 1

    @A(25)
    fun sdfgkjnsdjfg(s : String) : Int = 1

    @A(26)
    fun sfgkndfkgj(s : String) : Int = 1

    @A(27)
    fun sdfngksd(s : String) : Int = 1

    @A(28)
    fun sdfg23(s : String) : Int = 1

    @A(29)
    fun sgdfnknsdf(s : String) : Int = 1

    @A(30)
    fun sdfkjgnfnjdsg(s : String) : Int = 1

    @A(31)
    fun sdfijogdsgf(s : String) : Int = 1

    @A(32)
    fun sdfkgndgf(s : String) : Int = 1

    @A(33)
    fun fkgdnskndf(s : String) : Int = 1


}