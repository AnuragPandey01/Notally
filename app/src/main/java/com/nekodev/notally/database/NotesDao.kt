package com.nekodev.notally.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotesDao{

    @Insert
    suspend fun insertNote(notes : Notes)

    @Update
    suspend fun updateNote(notes: Notes)

    @Delete
    suspend fun deleteNote(notes: Notes)

    @Query("SELECT * FROM Notes" )
    fun getNotes() : LiveData<List<Notes>>
}
