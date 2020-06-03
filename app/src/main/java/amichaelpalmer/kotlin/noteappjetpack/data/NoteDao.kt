package amichaelpalmer.kotlin.noteappjetpack.data

import androidx.lifecycle.LiveData
import androidx.room.*

// @Dao: Data access object used by Room
@Dao
interface NoteDao {
    // Method signatures for all the operations we need

    // Suspend: keyword indicates this method may be run in a coroutine

    // Annotations generate the code for us
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM note_table")
    suspend fun deleteAllNotes()

    // LiveData - we can observe this object and be able to notify the activity/fragment
    @Query("SELECT * FROM NOTE_TABLE ORDER BY priority DESC")
    suspend fun getAllNotes(): LiveData<List<Note>>
}