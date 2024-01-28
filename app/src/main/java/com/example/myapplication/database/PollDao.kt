package com.example.myapplication.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PollDao {

    @Insert
    suspend fun insert(entry: Entry)

    @Update
    suspend fun update(entry: Entry)

    // TODO for History
    @Query("SELECT * FROM poll_table WHERE ishistory = 'true'")
    fun getHistoryData(): List<Entry>


    @Query("SELECT * FROM poll_table WHERE ishistory = :flagValue")
    fun getItemsByFlag(flagValue: Boolean):LiveData<List<Entry>>


    @Query("SELECT * FROM poll_table WHERE question = :s")
    fun isExist(s: String): Boolean
}