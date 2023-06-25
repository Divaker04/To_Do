package com.example.to_do

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "date_time_table")
data class Date_Time_Data(

    @PrimaryKey(autoGenerate = true)

                           val id :Long,
                           val id_of_message:String,
                           val date_time:String)
