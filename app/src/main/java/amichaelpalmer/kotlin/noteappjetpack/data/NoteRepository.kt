package amichaelpalmer.kotlin.noteappjetpack.data

import android.app.Application

class NoteRepository(private val application: Application) {
    private val noteDao: NoteDao by lazy { NoteDatabase.getInstance(application)!!.noteDao() } // todo: without doublebang?

    // Note Room only allows calls off the main thread to prevent freezing. We use coroutines
    // These represent the API exposed by the repository to the ViewModel(s)
    suspend fun insertOrUpdate(note: Note) {
        noteDao.insertOrUpdate(note)
    }

    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }

    suspend fun deleteAllNotes() {
        noteDao.deleteAllNotes()
    }

    suspend fun getAllNotes(): List<Note> {
        return noteDao.getAllNotes()
    }

}