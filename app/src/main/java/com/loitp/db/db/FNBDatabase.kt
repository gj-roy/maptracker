package com.loitp.db.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.loitp.BuildConfig
import com.loitp.model.FloorPlan

@Database(entities = [FloorPlan::class], version = BuildConfig.VERSION_CODE)
abstract class FNBDatabase : RoomDatabase() {

    abstract fun floorPlanDao(): FloorPlanDao

    companion object {

        var instance: FNBDatabase? = null

        fun getInstance(context: Context): FNBDatabase? {
            if (instance == null) {
                synchronized(FNBDatabase::class) {
                    instance = Room.databaseBuilder(context.applicationContext, FNBDatabase::class.java, "MapTrackerDatabase")
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }

            return instance
        }

    }
}
