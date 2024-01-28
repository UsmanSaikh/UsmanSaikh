package com.example.myapplication.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "No History available. Contribute Polls and See Your Journey Here."
    }
    val text: LiveData<String> = _text
}