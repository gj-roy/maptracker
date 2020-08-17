package com.loitp.db

import android.app.Application
import com.loitp.db.db.AppDatabase
import com.loitp.model.ActionData
import com.loitp.model.ActionLiveData
import com.loitp.model.BaseViewModel
import com.loitp.model.History
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : BaseViewModel(application) {
    private val TAG = "loitpp" + javaClass.simpleName
    val saveHistoryActionLiveData: ActionLiveData<ActionData<ArrayList<History>>> = ActionLiveData()
    val getHistoryActionLiveData: ActionLiveData<ActionData<List<History>>> = ActionLiveData()
    val getByIndexHistoryActionLiveData: ActionLiveData<ActionData<List<History>>> = ActionLiveData()
    val deleteHistoryActionLiveData: ActionLiveData<ActionData<History>> = ActionLiveData()
    val updateHistoryActionLiveData: ActionLiveData<ActionData<History>> = ActionLiveData()
    val deleteAllHistoryActionLiveData: ActionLiveData<ActionData<Boolean>> = ActionLiveData()
    val insertHistoryActionLiveData: ActionLiveData<ActionData<History>> = ActionLiveData()
    val findHistoryActionLiveData: ActionLiveData<ActionData<History>> = ActionLiveData()

    private fun genHistory(id: String, name: String): History {
        val history = History()

        history.id = id
        history.fileName = name

        return history
    }

    private fun genListHistory(fromId: Int, toId: Int): ArrayList<History> {
        val listHistory = ArrayList<History>()
        for (i in fromId until toId) {
            listHistory.add(genHistory(id = "$i", name = "Name $i"))
        }
        return listHistory
    }

    fun saveListFrom0To10() {
        saveHistoryActionLiveData.set(ActionData(isDoing = true))

        ioScope.launch {

            val listHistory = genListHistory(fromId = 0, toId = 10)
            if (listHistory.isNotEmpty()) {
                AppDatabase.instance?.historyDao()?.insertListHistoryConflict(listHistory)

                saveHistoryActionLiveData.post(
                        ActionData(
                                isDoing = false,
                                isSuccess = true,
                                data = listHistory
                        )
                )
            }
        }
    }

    fun saveListFrom10To20() {
        saveHistoryActionLiveData.set(ActionData(isDoing = true))

        ioScope.launch {

            val listHistory = genListHistory(fromId = 10, toId = 20)
            if (listHistory.isNotEmpty()) {
                AppDatabase.instance?.historyDao()?.insertListHistoryConflict(listHistory)

                saveHistoryActionLiveData.post(
                        ActionData(
                                isDoing = false,
                                isSuccess = true,
                                data = listHistory
                        )
                )
            }
        }
    }

    fun getList() {
        getHistoryActionLiveData.set(ActionData(isDoing = true))
        ioScope.launch {
            val listHistory = AppDatabase.instance?.historyDao()?.getAllHistory()
            getHistoryActionLiveData.post(
                    ActionData(
                            isDoing = false,
                            isSuccess = true,
                            data = listHistory
                    )
            )
        }
    }

    fun getListByIndex(fromIndex: Int, toIndex: Int) {
        getByIndexHistoryActionLiveData.set(ActionData(isDoing = true))
        ioScope.launch {
            val offset = toIndex - fromIndex + 1
            val listHistory = AppDatabase.instance?.historyDao()?.getListHistoryByIndex(fromIndex = fromIndex, offset = offset)
            getByIndexHistoryActionLiveData.post(
                    ActionData(
                            isDoing = false,
                            isSuccess = true,
                            data = listHistory
                    )
            )
        }
    }

    fun deleteHistory(history: History) {
        ioScope.launch {
            deleteHistoryActionLiveData.post(ActionData(isDoing = true))
            AppDatabase.instance?.historyDao()?.delete(history)
            deleteHistoryActionLiveData.post(ActionData(isDoing = false, data = history))
        }
    }

    fun updateHistory(history: History) {
        ioScope.launch {
            updateHistoryActionLiveData.post(ActionData(isDoing = true))
            AppDatabase.instance?.historyDao()?.update(history)
            updateHistoryActionLiveData.post(ActionData(isDoing = false, data = history))
        }
    }

    fun deleteAll() {
        ioScope.launch {
            deleteAllHistoryActionLiveData.post(ActionData(isDoing = true))
            AppDatabase.instance?.historyDao()?.deleteAll()
            deleteAllHistoryActionLiveData.post(ActionData(isDoing = false, data = true))
        }
    }

    fun insertHistory() {
        ioScope.launch {
            insertHistoryActionLiveData.post(ActionData(isDoing = true))
            val id = System.currentTimeMillis()
            val history = genHistory(id = id.toString(), name = "Name $id")
            AppDatabase.instance?.historyDao()?.insert(history)
            insertHistoryActionLiveData.post(ActionData(isDoing = false, data = history))
        }
    }

    fun findId(id: String) {
        ioScope.launch {
            findHistoryActionLiveData.post(ActionData(isDoing = true))
            val history = AppDatabase.instance?.historyDao()?.find(id = id)
            findHistoryActionLiveData.post(ActionData(isDoing = false, data = history))
        }
    }
}
