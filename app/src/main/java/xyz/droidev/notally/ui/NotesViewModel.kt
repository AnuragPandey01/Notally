package xyz.droidev.notally.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import xyz.droidev.notally.data.local.database.Notes
import xyz.droidev.notally.data.local.database.NotesDao
import xyz.droidev.notally.data.local.database.NotesDataBase.Companion.getDatabaseInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: NotesDao
    private val _isEditMode: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isEditMode : LiveData<Boolean>
        get() = _isEditMode

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

    fun deleteNote(id: Int) {
        CoroutineScope(IO).launch {
            dao.deleteNote(id)
        }
    }

    suspend fun getNoteByID(id: Int): Notes = coroutineScope {
        val note = async {
            dao.getNote(id)
        }
        note.await()
    }

    fun setIsEditMode(boolean: Boolean){
        _isEditMode.value = boolean
    }


}