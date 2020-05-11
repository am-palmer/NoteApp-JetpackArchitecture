package amichaelpalmer.kotlin.noteappjetpack.viewmodel

import amichaelpalmer.kotlin.noteappjetpack.data.Note
import amichaelpalmer.kotlin.noteappjetpack.data.NoteRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository by lazy {
        NoteRepository(
            application
        )
    }
    private val allNotes by lazy { repository.getAllNotes }

    fun insert(note: Note) {
        repository.insert(note)
    }

    fun update(note: Note) {
        repository.update(note)
    }

    fun delete(note: Note) {
        repository.delete(note)
    }

    fun deleteAllNotes() {
        repository.deleteAllNotes()
    }

    fun getNoteList(): LiveData<List<Note>>{
        return allNotes
    }

}

class NoteViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoteViewModel(
            application
        ) as T
    }
}
