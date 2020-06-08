package amichaelpalmer.kotlin.noteappjetpack.viewmodel

import amichaelpalmer.kotlin.noteappjetpack.data.Note
import amichaelpalmer.kotlin.noteappjetpack.data.NoteDatabase
import amichaelpalmer.kotlin.noteappjetpack.data.NoteRepository
import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

// Coroutines are launched in the ViewModel to interact with the Room database

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository by lazy { NoteRepository.getInstance(NoteDatabase.getInstance(application.applicationContext).noteDao())
    }

    fun insert(note: Note) {
        viewModelScope.launch {
            repository.insertOrUpdate(note)
        }
    }

    fun delete(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            repository.deleteAllNotes()
        }
    }

    fun getNotes() = repository.getNotes()

}

class NoteViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoteViewModel(application) as T
    }
}
