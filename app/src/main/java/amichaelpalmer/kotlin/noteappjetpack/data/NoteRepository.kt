package amichaelpalmer.kotlin.noteappjetpack.data

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData

class NoteRepository(application: Application) {
    private val noteDao: NoteDao by lazy { NoteDatabase.getInstance(application)!!.noteDao() }
    private val allNotes: LiveData<List<Note>> by lazy { noteDao.getAllNotes() }

    // Note room doesn't allow these functions on the main thread as it could freeze the app. We have to use a second thread (asynctask here todo: replace asynctask with coroutine)
    // These represent the API exposed by the repository to the ViewModel(s)
    fun insertOrUpdate(note: Note): AsyncTask<Note, Unit, Unit>? {
        return InsertUpdateNoteAsyncTask(
            noteDao
        ).execute(note)
    }

//    fun update(note: Note) {
//        UpdateNoteAsyncTask(
//            noteDao
//        ).execute(note)
//    }

    fun delete(note: Note) {
        //Log.d(TAG, ".delete ")
        DeleteNoteAsyncTask(
            noteDao
        ).execute(note)
    }

    fun deleteAllNotes() {
        DeleteAllNotesAsyncTask(
            noteDao
        ).execute()
    }

    val getAllNotes get() = allNotes

    //  Inner Async task classes as object
    companion object {

        private const val TAG = "NoteRepository"

        private class InsertUpdateNoteAsyncTask(val noteDao: NoteDao) :
            AsyncTask<Note, Unit, Unit>() {
            override fun doInBackground(vararg notes: Note?) {
                noteDao.insertOrUpdate(notes[0]!!)
            }
        }

//        private class UpdateNoteAsyncTask(val noteDao: NoteDao) : AsyncTask<Note, Unit, Unit>() {
//            override fun doInBackground(vararg notes: Note?) {
//                noteDao.update(notes[0]!!)
//            }
//        }

        private class DeleteNoteAsyncTask(val noteDao: NoteDao) : AsyncTask<Note, Unit, Unit>() {
            override fun doInBackground(vararg notes: Note?) {
                //Log.d(TAG, ".deleteNoteAsyncTask: deleting note with title ${notes[0]!!.getTitle}")
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