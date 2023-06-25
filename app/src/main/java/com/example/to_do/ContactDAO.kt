package com.example.to_do

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactDAO {
    @Insert
   fun inserContact(contact:Contact)

    @Update
    fun updateContact(contact:Contact)


    @Query("DELETE FROM contact")
    fun deleteall()

    @Query("UPDATE contact SET name=:changedata WHERE id=:itemUid")
    fun updatetable(changedata:String,itemUid:String)

  /*  @Query("DELETE FROM contact WHERE name = :itemUid")
   suspend fun deleteByUid(itemUid: String)*/
  @Query("DELETE FROM contact WHERE id = :itemUid")
  suspend fun deleteByUid(itemUid: Long)

    @Delete
    fun deleteContact(contact:Contact)

    @Query("SELECT*FROM contact")
  fun getContact():List<Contact>

}