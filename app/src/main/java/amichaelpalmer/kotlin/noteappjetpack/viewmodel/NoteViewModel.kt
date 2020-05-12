package amichaelpalmer.kotlin.noteappjetpack.viewmodel

import amichaelpalmer.kotlin.noteappjetpack.data.Note
import amichaelpalmer.kotlin.noteappjetpack.data.NoteRepository
import android.app.Application
import android.util.Log
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
        repository.insertOrUpdate(note)
    }

//    fun update(note: Note) {
//        repository.update(note)
//    }

    fun delete(note: Note) {
        //Log.d(TAG, ".delete starts")
        repository.delete(note)
    }

    fun deleteAllNotes() {
        repository.deleteAllNotes()
    }

    fun getNoteList(): LiveData<List<Note>>{
        return allNotes
    }

    companion object{
        private const val TAG = "NoteViewModel"
    }

}

class NoteViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoteViewModel(
            application
        ) as T
    }
}
