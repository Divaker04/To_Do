package com.example.to_do

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "second_screen_table")
data class SecondData(
    @PrimaryKey(autoGenerate = true)
       val id :Long,
       val find_data:String,
       val second_screen_name :String)
