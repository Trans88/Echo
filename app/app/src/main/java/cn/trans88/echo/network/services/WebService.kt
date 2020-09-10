package cn.trans88.echo.network.services

import cn.trans88.common.ext.ensureDir
import cn.trans88.echo.AppContext
import cn.trans88.echo.network.interceptors.AcceptInterceptor
import cn.trans88.echo.network.interceptors.AuthInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory2
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.util.concurrent.TimeUnit

private const val BASE_URL ="https://api.github.com"

/**
 * 缓存请求
 */
private val cacheFile by lazy {
    File(AppContext.cacheDir,"webServiceApi").apply { ensureDir() }
}

//lazy在首次调用的时候会进行初始化
val retrofit by lazy {
    Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory2.createWithSchedulers (Schedulers.io(),AndroidSchedulers.mainThread()))
        .client(OkHttpClient.Builder()
            .connectTimeout(60,TimeUnit.SECONDS)
            .readTimeout(60,TimeUnit.SECONDS)
            .writeTimeout(60,TimeUnit.SECONDS)
            .cache(Cache(cacheFile,1024*1024*1024))
            .addInterceptor(AcceptInterceptor())
            .addInterceptor(AuthInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
        )
        .baseUrl(BASE_URL)
        .build()
}