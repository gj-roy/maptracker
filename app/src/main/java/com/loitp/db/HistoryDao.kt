package com.loitp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.loitp.db.BaseDao
import com.loitp.model.History

@Dao
interface HistoryDao : BaseDao<History> {

    @Query("SELECT * FROM history")
    fun getAllHistory(): List<History>

    @Query("SELECT * FROM history LIMIT :fromIndex,:offset")
    fun getListHistoryByIndex(fromIndex: Int, offset: Int): List<History>

    @Insert
    fun insertListHistory(list: ArrayList<History>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListHistoryConflict(list: ArrayList<History>)

    @Query("DELETE FROM history")
    suspend fun deleteAll()

    @Query("SELECT * FROM history WHERE id=:id")
    fun find(id: String): History?
}
