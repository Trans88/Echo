package cn.trans88.echo.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import cn.trans88.common.ext.no
import cn.trans88.common.ext.otherwise
import cn.trans88.common.log.LogUtil
import cn.trans88.echo.R
import cn.trans88.echo.model.account.AccountManager
import cn.trans88.echo.model.account.OnAccountStateChangeListener
import cn.trans88.echo.network.entities.User
import cn.trans88.echo.utils.doOnLayoutAvailable
import cn.trans88.echo.utils.loadWithGlide
import com.bennyhuo.tieguanyin.annotations.Builder
//import com.bennyhuo.tieguanyin.annotations.ActivityBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import splitties.alertdialog.appcompat.alertDialog
import splitties.init.appCtx
import splitties.toast.toast
import splitties.views.dsl.core.button
import splitties.views.imageResource
import splitties.views.onClick

@Builder(flags = [Intent.FLAG_ACTIVITY_CLEAR_TOP])
class MainActivity : AppCompatActivity() ,OnAccountStateChangeListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val toggle =ActionBarDrawerToggle(this,drawer_layout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawer_layout.setDrawerListener(toggle)
        toggle.syncState()
        initNavigationView()
        AccountManager.onAccountStateChangeListeners.add(this)
        title ="About"
    }

    private fun initNavigationView(){
        LogUtil.e("initNavigationView")
        AccountManager.currentUser?.let { updateNavigationView(it)}?: clearNavigationView()
        initNavigationHeaderEvent()
    }

    private fun initNavigationHeaderEvent(){
        navigationView.doOnLayoutAvailable {
            LogUtil.e("navigationView初始化")
            navigationHeader.onClick {
                LogUtil.e("点击navigationHeader")
                AccountManager.isLoggedIn().no {
                    startLoginActivity()
                }.otherwise {
                    AccountManager.logout()
                        .subscribe({
                            toast("注销成功")
                        },{
                            it.printStackTrace()
                        })
                }
            }
        }

    }

    private fun updateNavigationView(user: User){
        LogUtil.e("updateNavigationView")
        navigationView.doOnLayoutAvailable {
            usernameView.text =user.login
            emailView.text =user.bio?:""
            avatarView.loadWithGlide(user.avatar_url,user.login.first())
        }
    }

    private fun clearNavigationView(){
        LogUtil.e("clearNavigationView")
        navigationView.doOnLayoutAvailable {
            usernameView.text ="请登录"
            emailView.text =""
            avatarView.imageResource =R.drawable.ic_github
        }
    }

    override fun onLogin(user: User) {
        updateNavigationView(user)
    }

    override fun onLogout() {
       clearNavigationView()
    }

    override fun onDestroy() {
        super.onDestroy()
        AccountManager.onAccountStateChangeListeners.remove(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }

}
