package cn.trans88.common

import cn.trans88.common.ext.isYes
import cn.trans88.common.ext.otherwise
import org.junit.Assert
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun testBoolean(){
        val resultOtherwise =getBool().isYes {
            1
        }.otherwise {
            2
        }
        Assert.assertEquals(resultOtherwise,2)
    }

    fun getBool() = false
}
