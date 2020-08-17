package com.loitp.db.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<in T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(t: T): Long

    @Delete
    suspend fun delete(type: T)

    @Update
    suspend fun update(type: T)
}
