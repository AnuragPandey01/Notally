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

    @Query("SELECT * FROM Notes Order By id DESC" )
    fun getAllNotes() : LiveData<List<Notes>>

    @Query("SELECT * FROM NOTES WHERE id = :id")
    suspend fun getNote(id: Int): Notes
}
