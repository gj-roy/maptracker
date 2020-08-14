package com.loitp.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.core.base.BaseFragment
import com.core.utilities.LScreenUtil
import com.loitp.R

class HomeFragment : BaseFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addFragment(HistoryFragment())
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_home
    }

    override fun setTag(): String? {
        return javaClass.simpleName
    }

    fun addFragment(fragment: Fragment) {
        activity?.let {
            LScreenUtil.addFragment(activity = it, containerFrameLayoutIdRes = R.id.flContainer, fragment = fragment, isAddToBackStack = false)
        }
    }
}
