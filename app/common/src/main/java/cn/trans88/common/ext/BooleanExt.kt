package cn.trans88.common.ext

//Out是协变，表示泛型类的继承关系与泛型实参的继承关系相同
sealed class BooleanExt<out T>//todo 这里为什么要加协变，不加协变好像也不会有问题

object Otherwise : BooleanExt<Nothing>()
class WithData<T>(val data: T) : BooleanExt<T>()

inline fun<T> Boolean.isYes(block: () -> T) =
    when {
        this -> {
            WithData(block())
        }
        else -> {
            Otherwise
        }
    }

inline fun <T> Boolean.isNo(block: () -> T) = when {
    this -> Otherwise
    else -> {
        WithData(block())
    }
}

inline fun<T> BooleanExt<T>.otherwise(block: () -> T):T=
    when(this){
        is Otherwise ->block()
        is WithData ->this.data
    }

