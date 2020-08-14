package com.loitp.fragment

import android.os.Bundle
import android.view.View
import com.core.base.BaseFragment
import com.loitp.R

class MapFragment : BaseFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_map
    }

    override fun setTag(): String? {
        return javaClass.simpleName
    }
}
