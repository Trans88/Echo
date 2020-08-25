package cn.trans88.echo

import cn.trans88.common.ext.Preference

object Settings {
    var username:String by Preference(AppContext,"username","")//用户名
    var password:String by Preference(AppContext,"password","")//用户密码
}