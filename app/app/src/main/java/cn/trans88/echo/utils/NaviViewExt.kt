package cn.trans88.echo.utils

import android.annotation.SuppressLint

import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.view.menu.MenuItemImpl
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import cn.trans88.common.ext.otherwise
import cn.trans88.common.ext.yes
import com.bennyhuo.common.log.logger
import com.google.android.material.navigation.NavigationView

/**
 * Created by benny on 7/6/17.
 */
inline fun NavigationView.doOnLayoutAvailable(crossinline block: () -> Unit) {
    ViewCompat.isLaidOut(this).yes {
        block()
    }.otherwise {
        addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                removeOnLayoutChangeListener(this)
                block()
            }
        })
    }
}

inline fun DrawerLayout.afterClosed(crossinline block: () -> Unit){
    if(isDrawerOpen(GravityCompat.START)) {
        closeDrawer(GravityCompat.START)
        addDrawerListener(
                object : DrawerLayout.DrawerListener {
                    override fun onDrawerStateChanged(newState: Int) = Unit
                    override fun onDrawerSlide(drawerView: View, slideOffset: Float) = Unit
                    override fun onDrawerOpened(drawerView: View) = Unit

                    override fun onDrawerClosed(drawerView: View) {
                        removeDrawerListener(this)
                        block()
                    }
                })
    } else {
        block()
    }
}
