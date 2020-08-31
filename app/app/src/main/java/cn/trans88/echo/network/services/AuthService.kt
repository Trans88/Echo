package cn.trans88.echo.network.services

import cn.trans88.echo.network.entities.AuthorizationReq
import cn.trans88.echo.network.entities.AuthorizationRsp
import cn.trans88.echo.settings.Configs
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PUT
import retrofit2.http.Path
import rx.Observable
import java.util.*

interface AuthApi{
    @PUT("/authorizations/clients/${Configs.Account.clientId}/{fingerPrint}")
    fun createAuthorization(@Body req:AuthorizationReq,@Path("fingerPrint") fingerPrint:String =Configs.Account.fingerPrint):Observable<AuthorizationRsp>
    @DELETE("authorizations/{id}")
    fun deleteAuthorization(@Path("id")id :Int):Observable<Response<Any>>
}

object AuthService:AuthApi by retrofit.create(AuthApi::class.java)