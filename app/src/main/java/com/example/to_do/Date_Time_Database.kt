package com.example.to_do

import androidx.room.Database
import androidx.room.Entity
import androidx.room.RoomDatabase

@Database(entities = [Date_Time_Data::class], version = 3)
abstract class Date_Time_Database:RoomDatabase() {
    abstract fun DateTime_Dao():Date_Time_DAO
}