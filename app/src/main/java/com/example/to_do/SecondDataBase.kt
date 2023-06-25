package com.example.to_do

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SecondData::class], version = 2)
abstract class SecondDataBase:RoomDatabase() {
    abstract fun secondScreenDao():Second_DAO
}