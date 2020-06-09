package amichaelpalmer.kotlin.noteappjetpack.utils

import amichaelpalmer.kotlin.noteappjetpack.data.NoteDatabase
import amichaelpalmer.kotlin.noteappjetpack.data.NoteRepository
import amichaelpalmer.kotlin.noteappjetpack.viewmodel.NoteViewModelFactory
import android.content.Context
import androidx.fragment.app.Fragment

object InjectorUtils {

    private fun getNoteRepository(context: Context): NoteRepository {
        return NoteRepository.getInstance(
            NoteDatabase.getInstance(context.applicationContext).noteDao()
        )
    }

    fun provideNoteViewModelFactory(fragment: Fragment): NoteViewModelFactory {
        val repository = getNoteRepository(fragment.requireContext())
        return NoteViewModelFactory(repository, fragment)
    }
}