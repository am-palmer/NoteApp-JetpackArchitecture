package amichaelpalmer.kotlin.noteappjetpack.data

class NoteRepository private constructor(private val noteDao: NoteDao) {
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

    fun getNotes() = noteDao.getAllNotes()

    companion object {
        // Singleton instantiation
        @Volatile
        private var instance: NoteRepository? = null

        fun getInstance(noteDao: NoteDao) =
            instance ?: synchronized(this) {
                instance ?: NoteRepository(noteDao).also { instance = it }
            }
    }

}