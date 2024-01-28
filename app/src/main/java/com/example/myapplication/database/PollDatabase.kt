package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Entry::class], version = 1)
@TypeConverters(Converter::class)
public abstract class PollDatabase : RoomDatabase() {

    abstract fun expanseDao(): PollDao

    companion object {
        private var INSTANCE: PollDatabase? = null
        fun getDatabase(context: Context): PollDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(context,
                        PollDatabase::class.java,
                        "poll_database"
                    )
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}