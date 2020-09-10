package cn.trans88.echo.network.interceptors

import android.util.Base64
import cn.trans88.echo.model.account.AccountManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 登陆鉴权拦截器
 */
class AuthInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original =chain.request()
        return chain.proceed(original.newBuilder()
            .apply {
                when{
                    original.url().pathSegments().contains("authorizations")->{
                        val userCredentials = "${AccountManager.userName}:${AccountManager.password}"
                        val auth ="Basic ${String(Base64.encode(userCredentials.toByteArray(),Base64.DEFAULT))}".trim()
                        header("Authorization",auth)
                    }
                    AccountManager.isLoggedIn() ->{
                        val auth ="Token ${AccountManager.token}"
                        header("Authorization",auth)
                    }

                    else ->removeHeader("Authorization")
                }
            }
            .build())
    }
}