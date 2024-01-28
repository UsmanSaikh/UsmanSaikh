package com.example.myapplication.database

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

@Keep
@Entity(tableName = "poll_table")
class Entry(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "question") val question: String,
    @ColumnInfo(name = "option") val option: ArrayList<String>,
    @ColumnInfo(name = "ishistory") val ishistory: Boolean,
    @ColumnInfo(name = "answer") val answer: Int,

)
