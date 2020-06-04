package amichaelpalmer.kotlin.noteappjetpack.data

import androidx.room.*

// @Dao: Data access object used by Room
// Note that Room runs suspend functions on a custom main-safe dispatcher: should NOT use withContext(IO) when running these functions
@Dao
interface NoteDao {

    // Annotations generate the code for us
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM note_table")
    suspend fun deleteAllNotes()

    // LiveData - we can observe this object and be able to notify the activity/fragment
    @Query("SELECT * FROM NOTE_TABLE ORDER BY priority DESC")
    suspend fun getAllNotes(): List<Note>
}