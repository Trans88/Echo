package cn.trans88.echo.utils

import cn.trans88.echo.ui.AppCompatAvatarImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Created by benny on 7/4/17.
 */
fun AppCompatAvatarImageView.loadWithGlide(url: String, textPlaceHolder: Char, requestOptions: RequestOptions = RequestOptions()){
    //没有图片时通过字符随机生成一个颜色，并把这个字符作为图片显示
    textPlaceHolder.toString().let {
        setTextAndColorSeed(it.toUpperCase(), it.hashCode().toString())
    }

    Glide.with(this.context)
            .load(url)
            .apply(requestOptions)
            .into(this)
}