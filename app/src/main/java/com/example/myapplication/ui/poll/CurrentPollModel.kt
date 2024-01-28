package com.example.myapplication.ui.poll

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CurrentPollModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "The Poling Grounds Await Your Questions"
    }
    val text: LiveData<String> = _text
}