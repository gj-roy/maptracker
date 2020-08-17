package com.loitp.db

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.base.BaseFontActivity
import com.loitp.R
import com.loitp.app.LApplication
import com.loitp.model.History
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_database_room.*

class RoomActivity : BaseFontActivity() {
    private var roomHistoryAdapter: RoomHistoryAdapter? = null
    private var homeViewModel: HomeViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setupViewModels()
    }

    override fun setFullScreen(): Boolean {
        return false
    }

    override fun setTag(): String? {
        return  javaClass.simpleName
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_database_room
    }

    private fun setupView() {
        roomHistoryAdapter = RoomHistoryAdapter()
        roomHistoryAdapter?.apply {
            onClickRootView = {
                logD(LApplication.gson.toJson(it))
                showShort(LApplication.gson.toJson(it))
            }
            onClickUpDate = {
                handleUpdate(it)
            }
            onClickDelete = {
                handleDelete(it)
            }
        }
        rv.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        rv.layoutManager = LinearLayoutManager(activity)
        rv.adapter = roomHistoryAdapter

        btInsert.setSafeOnClickListener {
            handleInsert()
        }
        btSaveListFrom0To10.setSafeOnClickListener {
            handleSaveListFrom0To10()
        }
        btSaveListFrom10To20.setSafeOnClickListener {
            handleSaveListFrom10To20()
        }
        btGetList.setSafeOnClickListener {
            handleGetList()
        }
        btGetListFrom3To5.setSafeOnClickListener {
            handleGetListFrom3To5()
        }
        btDeleteAll.setSafeOnClickListener {
            handleDeleteAll()
        }
        btFind1.setSafeOnClickListener {
            handleFind1()
        }
    }

    private fun setupViewModels() {
        homeViewModel = getViewModel(HomeViewModel::class.java)
        homeViewModel?.let { hvm ->
            hvm.saveHistoryActionLiveData.observe(this, Observer { actionData ->
                actionData.isDoing?.let {
                    if (it) {
                        progressBar.visibility = View.VISIBLE
                    } else {
                        progressBar.visibility = View.GONE
                    }
                }
                actionData.data?.let {
                    logD("saveHistoryActionLiveData observe " + LApplication.gson.toJson(it))
                    handleGetList()
                }
            })

            hvm.getHistoryActionLiveData.observe(this, Observer { actionData ->
                actionData.isDoing?.let {
                    if (it) {
                        progressBar.visibility = View.VISIBLE
                    } else {
                        progressBar.visibility = View.GONE
                    }
                }
                actionData.data?.let {
                    logD("getHistoryActionLiveData observe " + LApplication.gson.toJson(it))
                    roomHistoryAdapter?.setListHistory(it)
                }
            })

            hvm.getByIndexHistoryActionLiveData.observe(this, Observer { actionData ->
                actionData.isDoing?.let {
                    if (it) {
                        progressBar.visibility = View.VISIBLE
                    } else {
                        progressBar.visibility = View.GONE
                    }
                }
                actionData.data?.let {
                    logD("getByIndexHistoryActionLiveData observe " + LApplication.gson.toJson(it))
                    showShort("getByIndexHistoryActionLiveData:\n" + LApplication.gson.toJson(it))
                }
            })

            hvm.deleteHistoryActionLiveData.observe(this, Observer { actionData ->
                actionData.isDoing?.let {
                    if (it) {
                        progressBar.visibility = View.VISIBLE
                    } else {
                        progressBar.visibility = View.GONE
                    }
                }
                actionData.data?.let {
                    handleGetList()
                }
            })

            hvm.updateHistoryActionLiveData.observe(this, Observer { actionData ->
                actionData.isDoing?.let {
                    if (it) {
                        progressBar.visibility = View.VISIBLE
                    } else {
                        progressBar.visibility = View.GONE
                    }
                }
                actionData.data?.let {
                    handleGetList()
                }
            })

            hvm.deleteAllHistoryActionLiveData.observe(this, Observer { actionData ->
                actionData.isDoing?.let {
                    if (it) {
                        progressBar.visibility = View.VISIBLE
                    } else {
                        progressBar.visibility = View.GONE
                    }
                }
                actionData.data?.let {
                    if (it) {
                        handleGetList()
                    }
                }
            })

            hvm.insertHistoryActionLiveData.observe(this, Observer { actionData ->
                actionData.isDoing?.let {
                    if (it) {
                        progressBar.visibility = View.VISIBLE
                    } else {
                        progressBar.visibility = View.GONE
                    }
                }
                actionData.data?.let {
                    handleGetList()
                }
            })

            hvm.findHistoryActionLiveData.observe(this, Observer { actionData ->
                val isDoing = actionData.isDoing
                isDoing?.let {
                    if (it) {
                        progressBar.visibility = View.VISIBLE
                    } else {
                        progressBar.visibility = View.GONE
                    }
                }
//                val data = actionData.data
                if (isDoing == false) {
                    showShort("findHistoryActionLiveData observe " + LApplication.gson.toJson(actionData.data))
                }
            })
        }
    }

    private fun handleInsert() {
        homeViewModel?.insertHistory()
    }

    private fun handleSaveListFrom0To10() {
        homeViewModel?.saveListFrom0To10()
    }

    private fun handleSaveListFrom10To20() {
        homeViewModel?.saveListFrom10To20()
    }

    private fun handleGetList() {
        homeViewModel?.getList()
    }

    private fun handleGetListFrom3To5() {
        homeViewModel?.getListByIndex(fromIndex = 3, toIndex = 5)
    }

    private fun handleDelete(history: History) {
        homeViewModel?.deleteHistory(history)
    }

    private fun handleUpdate(history: History) {
        history.fileName = "Update Name " + System.currentTimeMillis()
        homeViewModel?.updateHistory(history)
    }

    private fun handleDeleteAll() {
        homeViewModel?.deleteAll()
    }

    private fun handleFind1() {
        homeViewModel?.findId(id = "1")
    }

}
