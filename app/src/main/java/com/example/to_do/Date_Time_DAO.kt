package com.example.to_do

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
@Dao
interface Date_Time_DAO {

    @Insert
    fun insertdatetimeintable(datetimeData: Date_Time_Data)

    @Query("DELETE FROM date_time_table WHERE id_of_message = :itemUid")
    suspend fun deleteByUiddatetime(itemUid: Long)

//"SELECT*FROM date_time_table where id_of_message=:position"

    @Query("SELECT*FROM date_time_table where id_of_message=:position")
    fun getdatetimedata(position: String):List<Date_Time_Data>
}

//28/4/2023  15:12