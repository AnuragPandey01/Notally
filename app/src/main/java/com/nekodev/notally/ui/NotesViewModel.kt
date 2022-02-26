package com.nekodev.notally.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.nekodev.notally.database.Notes
import com.nekodev.notally.database.NotesDao
import com.nekodev.notally.database.NotesDataBase.Companion.getDatabaseInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: NotesDao

    init {
        dao = getDatabaseInstance(application).noteDao()
    }

    fun getAllNotes(): LiveData<List<Notes>> {
        return dao.getAllNotes()
    }

    fun updateNote(note: Notes) {
        CoroutineScope(IO).launch {
            dao.updateNote(note)
        }
    }

    fun insertNote(note: Notes) {
        CoroutineScope(IO).launch {
            dao.insertNote(note)
        }
    }

    fun deleteNote(note: Notes) {
        CoroutineScope(IO).launch {
            dao.deleteNote(note)
        }
    }

}