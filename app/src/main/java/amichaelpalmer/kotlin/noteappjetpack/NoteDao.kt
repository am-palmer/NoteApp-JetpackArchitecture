package amichaelpalmer.kotlin.noteappjetpack

import amichaelpalmer.kotlin.noteappjetpack.Note
import androidx.lifecycle.LiveData
import androidx.room.*

// Data access object used by Room
// @Dao annotation - tells room this is a Dao
@Dao
interface NoteDao {
    // Method signatures for all the operations we need

    // Annotations generate the code for us
    @Insert
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    // @Query annotation used for custom
    @Query("DELETE FROM note_table")
    fun deleteAllNotes()

    // LiveData - we can observe this object and be able to notify the activity/fragment
    @Query("SELECT * FROM NOTE_TABLE ORDER BY priority DESC")
    fun getAllNotes(): LiveData<List<Note>>
}