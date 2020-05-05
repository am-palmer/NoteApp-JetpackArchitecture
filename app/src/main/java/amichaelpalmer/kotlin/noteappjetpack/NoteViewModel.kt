package amichaelpalmer.kotlin.noteappjetpack

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository by lazy { NoteRepository(application) }
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

    fun deleteAll() {
        repository.deleteAllNotes()
    }

    val getAllNotes get() = allNotes

}