package com.example.to_do
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface Second_DAO {
    @Insert
    fun insertdatasecondscreen(secondData: SecondData)

    @Query("DELETE FROM second_screen_table WHERE id = :itemUid")
    suspend fun deleteByUid(itemUid: Long)

    @Query("SELECT*FROM second_screen_table where find_data=:position")
    fun getdatasecondscreen(position: String):List<SecondData>


    @Query("UPDATE second_screen_table SET second_screen_name=:changedata WHERE id=:itemUid")
    fun updatetable(changedata:String,itemUid:Long)

}