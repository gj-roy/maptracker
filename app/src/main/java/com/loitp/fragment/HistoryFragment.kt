package com.loitp.fragment

import android.app.Activity
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
    private var pageIndex = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupViewModels()

        loadData()
    }

    private fun loadData() {
        homeViewModel?.getListByPage(pageIndex = pageIndex)
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
                        pageIndex++
                        loadData()
                        showShort("onLoadMore")
                    }
                })
        rv.layoutManager = LinearLayoutManager(activity)
        rv.adapter = historyAdapter

        btRecord.setSafeOnClickListener {
            activity?.let { a ->
                val intent = Intent(a, MapActivity::class.java)
                startActivityForResult(intent, KeyConstant.REQUEST_CODE_MAP)
                LActivityUtil.tranIn(context = a)
            }
        }
    }

    private fun setupViewModels() {
        homeViewModel = getViewModel(HomeViewModel::class.java)
        homeViewModel?.getHistoryPageActionLiveData?.observe(viewLifecycleOwner, Observer { actionData ->
//            logD("getHistoryPageActionLiveData " + LApplication.gson.toJson(actionData))
            val isDoing = actionData.isDoing
            if (isDoing == true) {
                indicatorView.smoothToShow()
            } else {
                indicatorView.smoothToHide()
                val data = actionData.data
                logD("getHistoryPageActionLiveData ${data?.size} " + LApplication.gson.toJson(data))
                if (data.isNullOrEmpty()) {
                    if (listHistory.isNullOrEmpty()) {
                        tvNoData.visibility = View.VISIBLE
                    }
                } else {
                    tvNoData.visibility = View.GONE
                    listHistory.addAll(data)
                    historyAdapter?.notifyDataSetChanged()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == KeyConstant.REQUEST_CODE_MAP && resultCode == Activity.RESULT_OK) {
            listHistory.clear()
            historyAdapter?.notifyDataSetChanged()
            pageIndex = 0
            loadData()
        }
    }
}
