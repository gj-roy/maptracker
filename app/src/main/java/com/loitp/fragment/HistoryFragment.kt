package com.loitp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.base.BaseFragment
import com.core.utilities.LActivityUtil
import com.loitp.R
import com.loitp.activity.DetailActivity
import com.loitp.activity.MapActivity
import com.loitp.adapter.HistoryAdapter
import com.loitp.app.LApplication
import com.loitp.model.History
import com.loitp.util.KeyConstant
import com.loitp.viewmodel.HomeViewModel
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.frm_history.*
import java.util.*

class HistoryFragment : BaseFragment() {

    private val listHistory = ArrayList<History>()
    private var historyAdapter: HistoryAdapter? = null
    private var homeViewModel: HomeViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logD("onViewCreated")
        setupViews()
        setupViewModels()

        homeViewModel?.getListByIndex(0, 100)//3 item
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_history
    }

    override fun setTag(): String? {
        return "loitpp" + javaClass.simpleName
    }

    private fun setupViews() {
        historyAdapter = HistoryAdapter(
                moviesList = listHistory,
                callback = object : HistoryAdapter.Callback {
                    override fun onClick(history: History, position: Int) {
                        activity?.let {
                            val intent = Intent(it, DetailActivity::class.java)
                            intent.putExtra(KeyConstant.KEY_HISTORY, history)
                            startActivity(intent)
                            LActivityUtil.tranIn(it)
                        }
                    }

                    override fun onLoadMore() {
                        //TODO
                        showShort("onLoadMore")
                    }
                })
        rv.layoutManager = LinearLayoutManager(activity)
        rv.adapter = historyAdapter

        btRecord.setSafeOnClickListener {
            activity?.let { a ->
                val intent = Intent(a, MapActivity::class.java)
                startActivity(intent)
                LActivityUtil.tranIn(context = a)
            }
        }
    }

    private fun setupViewModels() {
        homeViewModel = getViewModel(HomeViewModel::class.java)
        homeViewModel?.getByIndexHistoryActionLiveData?.observe(viewLifecycleOwner, Observer { actionData ->
            logD("getByIndexHistoryActionLiveData " + LApplication.gson.toJson(actionData))
            val isDoing = actionData.isDoing
            if (isDoing == true) {
                indicatorView.smoothToShow()
            } else {
                indicatorView.smoothToHide()
                val data = actionData.data
                logD("getByIndexHistoryActionLiveData ${data?.size} " + LApplication.gson.toJson(data))
                if (data.isNullOrEmpty()) {
                    tvNoData.visibility = View.VISIBLE
                } else {
                    tvNoData.visibility = View.GONE
                    listHistory.addAll(data.reversed())
                    historyAdapter?.notifyDataSetChanged()
                }
            }
        })
    }
}
