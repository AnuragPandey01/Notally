package com.nekodev.notally.ui

import android.app.Application
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.nekodev.notally.database.Notes
import com.nekodev.notally.database.NotesDao
import com.nekodev.notally.database.NotesDataBase.Companion.getDatabaseInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val dao : NotesDao
    private val _noteList = mutableListOf<Notes>()
    val noteList : List<Notes>
        get() = _noteList

    init {
        _noteList.add(Notes(0,"meow,not working","h","20may"))
        dao = getDatabaseInstance(application).noteDao()
    }

    fun getAllNotes() : LiveData<List<Notes>> {
        return dao.getNotes()
    }

    fun updateNote(note: Notes){
        CoroutineScope(IO).launch {
            dao.updateNote(note)
        }
    }

    fun insertNote(note: Notes){
        Log.d("meow", "insertNote: $note")
        CoroutineScope(IO).launch {
            Log.d("meow", "insert: $note")
            dao.insertNote(note)
            Log.d("meow", "getNote: ${getAllNotes().value}")
        }
    }

    fun deleteNote(note: Notes){
        CoroutineScope(IO).launch {
            dao.deleteNote(note)
        }
    }

}