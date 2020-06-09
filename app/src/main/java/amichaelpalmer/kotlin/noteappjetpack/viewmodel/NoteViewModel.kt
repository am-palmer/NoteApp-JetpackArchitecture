package amichaelpalmer.kotlin.noteappjetpack.viewmodel

import amichaelpalmer.kotlin.noteappjetpack.data.Note
import amichaelpalmer.kotlin.noteappjetpack.data.NoteRepository
import android.os.Bundle
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.coroutines.launch

// Coroutines are launched in the ViewModel to interact with the Room database

class NoteViewModel internal constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    fun insert(note: Note) {
        viewModelScope.launch {
            noteRepository.insertOrUpdate(note)
        }
    }

    fun delete(note: Note) {
        viewModelScope.launch {
            noteRepository.delete(note)
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            noteRepository.deleteAllNotes()
        }
    }

    fun getNotes() = noteRepository.getNotes()

}

class NoteViewModelFactory(
    private val repository: NoteRepository,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return NoteViewModel(repository) as T
    }

}
