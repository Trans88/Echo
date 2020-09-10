package cn.trans88.echo.model.account

import cn.trans88.echo.network.entities.AuthorizationReq
import cn.trans88.echo.network.entities.AuthorizationRsp
import cn.trans88.echo.network.entities.User
import cn.trans88.echo.network.services.AuthService
import cn.trans88.echo.network.services.UserService
import cn.trans88.echo.utils.fromJson
import cn.trans88.echo.utils.pref
import com.google.gson.Gson
import retrofit2.HttpException
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

interface OnAccountStateChangeListener {
    fun onLogin(user:User)
    fun onLogout()
}

object AccountManager {
    var authId by pref(-1)
    var userName by pref("")
    var password by pref("")
    var token by pref("")

    private var userJson by pref("")
    var  currentUser :User? =null
    get() {
        if (field ==null && userJson.isNotEmpty()){
            field = Gson().fromJson<User>(userJson)
        }

        return field
    }
    set(value) {
        userJson = if (value ==null){
            ""
        }else{
            Gson().toJson(value)
        }
        field =value
    }

    val onAccountStateChangeListeners =ArrayList<OnAccountStateChangeListener>()

    fun isLoggedIn():Boolean = token.isNotEmpty()

    private fun notifyLogin(user: User){
        onAccountStateChangeListeners.forEach {
            it.onLogin(user)
        }
    }

    private fun notifyLogout(){
        onAccountStateChangeListeners.forEach { it.onLogout() }
    }

    fun login() =
        AuthService.createAuthorization(AuthorizationReq())
            .doOnNext {
                if (it.token.isEmpty()){
                    throw AccountException(it)
                }
            }
            .retryWhen {
                it.flatMap {
                   if(it is AccountException){
                       AuthService.deleteAuthorization(it.authorizationRsp.id)
                   }else{
                       Observable.error(it)
                   }
                }
            }
            .flatMap {
                token =it.token
                authId=it.id
                UserService.getAuthenticatedUser()
            }
            .map {
                currentUser =it
                notifyLogin(it)
            }
            .observeOn(AndroidSchedulers.mainThread())//在哪执行
            .subscribeOn(Schedulers.io())//在哪请求

    fun logout() =AuthService.deleteAuthorization(authId)
        .doOnNext {
            if (it.isSuccessful){
                authId =-1
                token =""
                currentUser =null
                notifyLogout()
            }else{
                throw HttpException(it)
            }
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    class AccountException(val authorizationRsp: AuthorizationRsp):Exception("Already logged in")
}