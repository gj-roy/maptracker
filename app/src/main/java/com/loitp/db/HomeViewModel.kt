package com.loitp.db

import android.app.Application
import com.loitp.db.db.FNBDatabase
import com.loitp.model.ActionData
import com.loitp.model.ActionLiveData
import com.loitp.model.BaseViewModel
import com.loitp.model.FloorPlan
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : BaseViewModel(application) {
    private val TAG = "loitpp" + javaClass.simpleName
    val saveFloorPlanActionLiveData: ActionLiveData<ActionData<ArrayList<FloorPlan>>> = ActionLiveData()
    val getFloorPlanActionLiveData: ActionLiveData<ActionData<List<FloorPlan>>> = ActionLiveData()
    val getByIndexFloorPlanActionLiveData: ActionLiveData<ActionData<List<FloorPlan>>> = ActionLiveData()
    val deleteFloorPlanActionLiveData: ActionLiveData<ActionData<FloorPlan>> = ActionLiveData()
    val updateFloorPlanActionLiveData: ActionLiveData<ActionData<FloorPlan>> = ActionLiveData()
    val deleteAllFloorPlanActionLiveData: ActionLiveData<ActionData<Boolean>> = ActionLiveData()
    val insertFloorPlanActionLiveData: ActionLiveData<ActionData<FloorPlan>> = ActionLiveData()
    val findFloorPlanActionLiveData: ActionLiveData<ActionData<FloorPlan>> = ActionLiveData()

    private fun genFloorPlan(id: String, name: String): FloorPlan {
        val floorPlan = FloorPlan()

        floorPlan.id = id
        floorPlan.name = name

        return floorPlan
    }

    private fun genListFloorPlan(fromId: Int, toId: Int): ArrayList<FloorPlan> {
        val listFloorPlan = ArrayList<FloorPlan>()
        for (i in fromId until toId) {
            listFloorPlan.add(genFloorPlan(id = "$i", name = "Name $i"))
        }
        return listFloorPlan
    }

    fun saveListFrom0To10() {
        saveFloorPlanActionLiveData.set(ActionData(isDoing = true))

        ioScope.launch {

            val listFloorPlan = genListFloorPlan(fromId = 0, toId = 10)
            if (listFloorPlan.isNotEmpty()) {
                FNBDatabase.instance?.floorPlanDao()?.insertListFloorPlanConflict(listFloorPlan)

                saveFloorPlanActionLiveData.post(
                        ActionData(
                                isDoing = false,
                                isSuccess = true,
                                data = listFloorPlan
                        )
                )
            } else {
                //handle error
                //floorPlanActionLiveData.postAction()
            }
        }
    }

    fun saveListFrom10To20() {
        saveFloorPlanActionLiveData.set(ActionData(isDoing = true))

        ioScope.launch {

            val listFloorPlan = genListFloorPlan(fromId = 10, toId = 20)
            if (listFloorPlan.isNotEmpty()) {
                FNBDatabase.instance?.floorPlanDao()?.insertListFloorPlanConflict(listFloorPlan)

                saveFloorPlanActionLiveData.post(
                        ActionData(
                                isDoing = false,
                                isSuccess = true,
                                data = listFloorPlan
                        )
                )
            } else {
                //handle error
                //floorPlanActionLiveData.postAction()
            }
        }
    }

    fun getList() {
        getFloorPlanActionLiveData.set(ActionData(isDoing = true))
        ioScope.launch {
            val listFloorPlan = FNBDatabase.instance?.floorPlanDao()?.getAllFloorPlan()
            getFloorPlanActionLiveData.post(
                    ActionData(
                            isDoing = false,
                            isSuccess = true,
                            data = listFloorPlan
                    )
            )
        }
    }

    fun getListByIndex(fromIndex: Int, toIndex: Int) {
        getByIndexFloorPlanActionLiveData.set(ActionData(isDoing = true))
        ioScope.launch {
            val offset = toIndex - fromIndex + 1
            val listFloorPlan = FNBDatabase.instance?.floorPlanDao()?.getListFloorPlanByIndex(fromIndex = fromIndex, offset = offset)
            getByIndexFloorPlanActionLiveData.post(
                    ActionData(
                            isDoing = false,
                            isSuccess = true,
                            data = listFloorPlan
                    )
            )
        }
    }

    fun deleteFloorPlan(floorPlan: FloorPlan) {
        ioScope.launch {
            deleteFloorPlanActionLiveData.post(ActionData(isDoing = true))
            FNBDatabase.instance?.floorPlanDao()?.delete(floorPlan)
            deleteFloorPlanActionLiveData.post(ActionData(isDoing = false, data = floorPlan))
        }
    }

    fun updateFloorPlan(floorPlan: FloorPlan) {
        ioScope.launch {
            updateFloorPlanActionLiveData.post(ActionData(isDoing = true))
            FNBDatabase.instance?.floorPlanDao()?.update(floorPlan)
            updateFloorPlanActionLiveData.post(ActionData(isDoing = false, data = floorPlan))
        }
    }

    fun deleteAll() {
        ioScope.launch {
            deleteAllFloorPlanActionLiveData.post(ActionData(isDoing = true))
            FNBDatabase.instance?.floorPlanDao()?.deleteAll()
            deleteAllFloorPlanActionLiveData.post(ActionData(isDoing = false, data = true))
        }
    }

    fun insertFloorPlan() {
        ioScope.launch {
            insertFloorPlanActionLiveData.post(ActionData(isDoing = true))
            val id = System.currentTimeMillis()
            val floorPlan = genFloorPlan(id = id.toString(), name = "Name $id")
            FNBDatabase.instance?.floorPlanDao()?.insert(floorPlan)
            insertFloorPlanActionLiveData.post(ActionData(isDoing = false, data = floorPlan))
        }
    }

    fun findId(id: String) {
        ioScope.launch {
            findFloorPlanActionLiveData.post(ActionData(isDoing = true))
            val floorPlan = FNBDatabase.instance?.floorPlanDao()?.find(id = id)
            findFloorPlanActionLiveData.post(ActionData(isDoing = false, data = floorPlan))
        }
    }
}
