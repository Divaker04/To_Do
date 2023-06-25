package com.example.to_do

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "Checkedbox_table")

data class Checked_box_Data(
    @PrimaryKey(autoGenerate = true)
    val id :Long,

    val name :String
)
