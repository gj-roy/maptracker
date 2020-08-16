package com.loitp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.base.BaseFragment
import com.core.common.Constants
import com.core.utilities.LActivityUtil
import com.loitp.R
import com.loitp.activity.MapActivity
import com.loitp.adapter.HistoryAdapter
import com.loitp.model.History
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.frm_history.*
import java.util.*

class HistoryFragment : BaseFragment() {

    private val listHistory = ArrayList<History>()
    private var historyAdapter: HistoryAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyAdapter = HistoryAdapter(
                moviesList = listHistory,
                callback = object : HistoryAdapter.Callback {
                    override fun onClick(history: History, position: Int) {
                        //TODO
                    }

                    override fun onLoadMore() {
                        //TODO
                        showShort("onLoadMore")
                    }
                })
        rv.layoutManager = LinearLayoutManager(activity)
        rv.adapter = historyAdapter

        loadData()

        btRecord.setSafeOnClickListener {
            activity?.let { a ->
                val intent = Intent(a, MapActivity::class.java)
                startActivity(intent)
                LActivityUtil.tranIn(context = a)
            }
        }
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_history
    }

    override fun setTag(): String? {
        return javaClass.simpleName
    }

    private fun loadData() {
        var cover: String
        for (i in 0..5) {
            cover = if (i % 2 == 0) {
                Constants.URL_IMG_1
            } else {
                Constants.URL_IMG_2
            }
            val history = History(distance = "Loitp $i", avgSpeed = "Action & Adventure $i", timer = "Year: $i", fileName = cover)
            listHistory.add(history)
        }
        historyAdapter?.notifyDataSetChanged()
    }
}
