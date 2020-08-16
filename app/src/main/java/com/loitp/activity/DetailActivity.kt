package com.loitp.activity

import android.os.Bundle
import com.core.base.BaseFontActivity
import com.loitp.R

class DetailActivity : BaseFontActivity() {

    override fun setTag(): String? {
        return javaClass.simpleName
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_detail
    }

    override fun setFullScreen(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}
