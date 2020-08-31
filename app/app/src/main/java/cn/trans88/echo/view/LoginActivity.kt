package cn.trans88.echo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.trans88.echo.R
import cn.trans88.echo.presenter.LoginPresenter
import cn.trans88.mvp.impl.BaseActivity

class LoginActivity : BaseActivity<LoginPresenter>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }
}
