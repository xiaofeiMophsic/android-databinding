package com.example.android.databinding.basicsample.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide

fun ImageView.displayWithUrl(ctx: Context, url: String?) {
    Glide.with(ctx).load(url).into(this)
}

inline fun <T: ViewDataBinding> T.executeAfter(block: T.() -> Unit) {
    block()
    executePendingBindings()
}