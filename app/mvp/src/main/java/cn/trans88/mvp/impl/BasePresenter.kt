package cn.trans88.mvp.impl

import android.content.res.Configuration
import android.os.Bundle
import cn.trans88.mvp.IMvpView
import cn.trans88.mvp.IPresenter

abstract class BasePresenter<out V:IMvpView<BasePresenter<V>>>:IPresenter<V> {
    //@UnsafeVariance 有时候泛型是协变但是你又想把它作为参数传入，或者有时是逆变的时候又想作为返回值返回，就用这个标签
    override lateinit var view: @UnsafeVariance V
    override fun onCreate(savedInstanceState: Bundle?) =Unit

    override fun onSaveInstanceState(outState: Bundle) =Unit

    override fun onViewStateRestored(savedInstanceState: Bundle?) =Unit

    override fun onConfigurationChanged(newConfig: Configuration) =Unit

    override fun onDestroy() =Unit

    override fun onStart() =Unit

    override fun onStop() =Unit

    override fun onResume() =Unit

    override fun onPause() =Unit
}