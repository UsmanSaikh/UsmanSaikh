package com.example.myapplication.database

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class Repository(private val pollDao: PollDao) {

    val pollList: LiveData<List<Entry>> = pollDao.getItemsByFlag(false)

    val pollListHistory: LiveData<List<Entry>> = pollDao.getItemsByFlag(true)

    fun isExist(s: String): Boolean {
        return pollDao.isExist(s)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(entry: Entry) {
        pollDao.insert(entry)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun upDate(entry: Entry) {
        pollDao.update(entry)
    }
}