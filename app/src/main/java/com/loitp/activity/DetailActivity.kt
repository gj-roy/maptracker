package com.loitp.activity

import android.os.Bundle
import com.core.base.BaseFontActivity
import com.core.common.Constants
import com.core.utilities.LImageUtil
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.model.History
import com.loitp.util.ImageUtil
import com.loitp.util.KeyConstant
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.view_item_history.view.*

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

        val obj = intent?.getSerializableExtra(KeyConstant.KEY_HISTORY)
        if (obj == null) {
            showShort(getString(R.string.err_unknow_en))
            onBackPressed()
        }
        if (obj is History) {
            val file = ImageUtil.getFile(obj.fileName)
            LImageUtil.load(context = activity, imageFile = file, imageView = ivMap)
            LImageUtil.setImageViewZoom(ivMap)
            tvDistance.text = obj.distance
            tvAvgSpeed.text = obj.avgSpeed
            tvTimer.text = obj.timer
        }
    }

}
