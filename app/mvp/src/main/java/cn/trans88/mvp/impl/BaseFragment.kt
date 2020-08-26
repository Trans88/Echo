package cn.trans88.mvp.impl

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.trans88.mvp.IMvpView
import cn.trans88.mvp.IPresenter
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

abstract class BaseFragment<out P:BasePresenter<BaseFragment<P>>>:Fragment(),IMvpView<P>{
    final override val presenter: P
    init {
        presenter =createPresenterKt()
        presenter.view =this
    }

    /**
     * kotlin版本
     */
    private fun createPresenterKt():P{
        sequence{
            var thisClass:KClass<*> =this@BaseFragment::class
            while (true){
                yield(thisClass.supertypes)
                thisClass =thisClass.supertypes.firstOrNull()?.jvmErasure?:break
            }
        }.flatMap{
            it.flatMap{kType->
                kType.arguments
            }.asSequence() //拿到泛式参数的list
        }.first{
            it.type?.jvmErasure?.isSubclassOf(IPresenter::class)?:false//筛选出来的必须是IPresenter的实例，否则不要
        }.let{
            return it.type!!.jvmErasure.primaryConstructor!!.call() as P
        }
    }

    /**
     * java版本
     */
    private fun createPresenter():P{
        sequence{
            var thisClass:Class<*> =this@BaseFragment.javaClass
            while (true){
                yield(thisClass.genericSuperclass)
                thisClass =thisClass.superclass?:break
            }
        }.filter{
            it is ParameterizedType
        }.flatMap{
            (it as ParameterizedType).actualTypeArguments.asSequence()
            //拿到泛式参数的list
        }.first{
            it is Class<*> && IPresenter::class.java.isAssignableFrom(it)
        }.let{
            return (it as Class<P>).newInstance()
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        presenter.onViewStateRestored(savedInstanceState)
    }
}