package com.example.to_do

import androidx.annotation.Size
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CheckBoxDAO {
    @Insert
    fun inserContact(checkedbox:Checked_box_Data)
    @Query("DELETE FROM Checkedbox_table WHERE name = :itemUid")
    suspend fun deleteByUid(itemUid: Long)

    //SELECT Coulmn1, Coulmn2, Coulmn3 FROM TABLENAME       SELECT*FROM Checkedbox_table where column name=name
    @Query("SELECT*FROM Checkedbox_table ")
    fun getdatetimedata():List<Checked_box_Data>
}