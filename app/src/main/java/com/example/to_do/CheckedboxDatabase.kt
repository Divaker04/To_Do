package com.example.to_do

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Checked_box_Data::class], version = 5)
abstract class CheckedboxDatabase:RoomDatabase() {
    abstract fun CheckedboxDao():CheckBoxDAO
}