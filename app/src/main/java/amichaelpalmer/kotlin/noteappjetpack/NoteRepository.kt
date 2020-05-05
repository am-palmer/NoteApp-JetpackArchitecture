package amichaelpalmer.kotlin.noteappjetpack

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class NoteRepository(application: Application) {
    private val noteDao: NoteDao by lazy { NoteDatabase.getInstance(application)!!.noteDao() }
    private val allNotes: LiveData<List<Note>> by lazy { noteDao.getAllNotes() }

    // Note room doesn't allow these functions on the main thread as it could freeze the app. We have to use a second thread (asynctask here todo: replace asynctask with coroutine)
    // These represent the API exposed by the repository to the ViewModel(s)
    fun insert(note: Note): AsyncTask<Note, Unit, Unit>? {
        return InsertNoteAsyncTask(noteDao).execute(note)
    }

    fun update(note: Note) {
        UpdateNoteAsyncTask(noteDao).execute(note)
    }

    fun delete(note: Note) {
        DeleteNoteAsyncTask(noteDao).execute(note)
    }

    fun deleteAllNotes() {
        DeleteAllNotesAsyncTask(noteDao).execute()
    }

    val getAllNotes get() = allNotes

    //  Inner Async task classes as object

    companion object {

        //private lateinit var noteDao: NoteDao // do we need this?

        private class InsertNoteAsyncTask(val noteDao: NoteDao) : AsyncTask<Note, Unit, Unit>() {
            override fun doInBackground(vararg notes: Note?) {
                noteDao.insert(notes[0]!!)
            }
        }

        private class UpdateNoteAsyncTask(val noteDao: NoteDao) : AsyncTask<Note, Unit, Unit>() {
            override fun doInBackground(vararg notes: Note?) {
                noteDao.update(notes[0]!!)
            }
        }

        private class DeleteNoteAsyncTask(val noteDao: NoteDao) : AsyncTask<Note, Unit, Unit>() {
            override fun doInBackground(vararg notes: Note?) {
                noteDao.delete(notes[0]!!)
            }
        }

        private class DeleteAllNotesAsyncTask(val noteDao: NoteDao) :
            AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg notes: Unit?) {
                noteDao.deleteAllNotes()
            }
        }
    }

}