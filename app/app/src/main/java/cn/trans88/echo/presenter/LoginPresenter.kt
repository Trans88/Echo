package cn.trans88.echo.presenter

import android.widget.Button
import cn.trans88.echo.R
import cn.trans88.echo.model.account.AccountManager
import cn.trans88.echo.view.LoginActivity
import cn.trans88.mvp.impl.BasePresenter
import kotlinx.android.synthetic.main.activity_login.view.*

class LoginPresenter: BasePresenter<LoginActivity>() {

    fun doLogin(name:String,password:String){
        AccountManager.userName =name
        AccountManager.password =password
        view.onLoginStart()
        AccountManager.login().subscribe({
            view.onLoginSuccess()
        },{
            view.onLoginError(it)
        })
    }

    fun checkUserName(name: String):Boolean{
        return true
    }

    fun checkPassword(password: String): Boolean {
        return true
    }

    override fun onResume() {
        super.onResume()
        view.onDataInit(AccountManager.userName,AccountManager.password)
    }
}