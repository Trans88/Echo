package cn.trans88.echo.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatEditText
import cn.trans88.common.ext.otherwise
import cn.trans88.common.ext.yes
import cn.trans88.echo.R
import cn.trans88.echo.presenter.LoginPresenter
import cn.trans88.mvp.impl.BaseActivity
import com.bennyhuo.common.ext.hideSoftInput
import com.bennyhuo.tieguanyin.annotations.Builder
import kotlinx.android.synthetic.main.activity_login.*
import splitties.toast.toast
import splitties.views.onClick

@Builder(flags = [Intent.FLAG_ACTIVITY_NO_HISTORY])
class LoginActivity : BaseActivity<LoginPresenter>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signInButton.onClick {
            presenter.checkUserName(username.text.toString())
                .yes {
                    presenter.checkPassword(password.text.toString())
                        .yes {
                            hideSoftInput()
                            presenter.doLogin(username.text.toString(),password.text.toString())
                        }
                        .otherwise {
                            showTips(password,"密码不合法")
                        }
                }
                .otherwise {
                    showTips(username,"用户名不合法")
                }
        }

    }

    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)
        loginForm.animate().setDuration(shortAnimTime.toLong()).alpha(
            (if (show) 0 else 1).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                loginForm.visibility = if (show) View.GONE else View.VISIBLE
            }
        })

        loginProgress.animate().setDuration(shortAnimTime.toLong()).alpha(
            (if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                loginProgress.visibility = if (show) View.VISIBLE else View.GONE
            }
        })
    }

    private fun showTips(view:EditText,tips:String){
        view.requestFocus()
        view.error =tips
    }

    fun onLoginStart(){
        showProgress(true)
    }
    fun onLoginError(e:Throwable){
        toast("登陆失败")
        showProgress(false)
    }

    fun onLoginSuccess(){
        toast("登陆成功")
        showProgress(false)
        startMainActivity()
        finish()
    }

    fun onDataInit(name:String,passwd:String){
        username.setText(name)
        password.setText(passwd)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
    }
}
