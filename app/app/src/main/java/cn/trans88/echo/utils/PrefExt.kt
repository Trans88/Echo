package cn.trans88.echo.utils

import cn.trans88.common.ext.Preference
import cn.trans88.echo.AppContext
import kotlin.reflect.jvm.jvmName

inline fun <reified R,T> R.pref(default:T) =Preference(AppContext,"",default,R::class.jvmName)