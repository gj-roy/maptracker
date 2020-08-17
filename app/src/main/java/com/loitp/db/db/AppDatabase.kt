package com.loitp.db.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.loitp.BuildConfig
import com.loitp.model.History

@Database(entities = [History::class], version = BuildConfig.VERSION_CODE)
abstract class AppDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao

    companion object {

        var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "MapTrackerDatabase")
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }

            return instance
        }

    }
}
