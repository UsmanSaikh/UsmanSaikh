package com.example.myapplication

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.example.myapplication.database.PollDatabase
import com.example.myapplication.database.Repository

class MyApplication : Application() {

    private val database by lazy { PollDatabase.getDatabase(this.applicationContext) }
    val repository by lazy { Repository(database.expanseDao()) }


    override fun onCreate() {
        super.onCreate()

        instance = this
        Log.d("MyApplication", "Application is initialized")
    }

    companion object {
        lateinit var instance: MyApplication

        fun getAppContext(): Context {
            return instance.applicationContext
        }
    }
}