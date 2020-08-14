package com.loitp.fragment

import android.os.Bundle
import android.view.View
import com.core.base.BaseFragment
import com.loitp.R
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.frm_history.*

class HistoryFragment : BaseFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btRecord.setSafeOnClickListener {
            if (parentFragment == null) {
                logD("parentFragment == null")
            }
            (parentFragment as HomeFragment).addFragment(MapFragment())
        }
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_history
    }

    override fun setTag(): String? {
        return "loitpp" + javaClass.simpleName
    }
}
