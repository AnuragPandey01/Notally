package com.nekodev.notally.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nekodev.notally.database.Notes
import com.nekodev.notally.database.NotesDao
import com.nekodev.notally.database.NotesDataBase.Companion.getDatabaseInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: NotesDao
    private val _isEditMode: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val isEditMode : LiveData<Boolean>
        get() = _isEditMode

    init {
        _isEditMode.value = true
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

    fun notifyModeChanged(){
        _isEditMode.value = !_isEditMode.value!!
    }


}