package com.asvetenco.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.asvetenco.database.dao.WorkoutDao
import com.asvetenco.database.entity.WorkoutEntity

@Database(entities = [WorkoutEntity::class], version = 3, exportSchema = false)
internal abstract class IntervalTimerDatabase : RoomDatabase() {

    abstract fun workoutDao(): WorkoutDao

    companion object {

        private const val DATABASE_NAME = "TweetsDatabase.db"

        @Volatile
        private var instance: IntervalTimerDatabase? = null

        fun getInstance(context: Context): IntervalTimerDatabase {
            return instance ?: synchronized(this) { buildDatabase(context).also { instance = it } }
        }

        fun clearTables() {
            instance?.clearAllTables()
        }

        private fun buildDatabase(context: Context): IntervalTimerDatabase {
            return Room.databaseBuilder(context, IntervalTimerDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}