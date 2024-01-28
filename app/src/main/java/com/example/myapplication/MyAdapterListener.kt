package com.example.myapplication

interface MyAdapterListener {
    fun onItemClicked(position: Int)
    fun onEditTextValueChanged(position: Int, value: String)
}