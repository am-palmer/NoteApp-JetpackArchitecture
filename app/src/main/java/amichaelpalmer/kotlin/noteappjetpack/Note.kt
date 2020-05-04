package amichaelpalmer.kotlin.noteappjetpack

import androidx.room.Entity
import androidx.room.PrimaryKey

// Represents a note object

// Room annotation - creates SQLITE table at compile time
@Entity(tableName = "note_table")
class Note(private val title: String, private val description: String, private val priority: Int) {

    // Not sure of access level
    // Allows us to uniquely identify each entry
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    val getTitle get() = title
    val getDescription get() = description
    val getPriority get() = priority

}