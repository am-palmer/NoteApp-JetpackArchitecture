package amichaelpalmer.kotlin.noteappjetpack.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

// Represents a note object

// Room annotation - creates SQLITE table at compile time
@Entity(tableName = "note_table")
open class Note(
    protected val title: String,
    protected val description: String,
    protected val priority: Int
) : Parcelable {

    // Allows us to uniquely identify each entry
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    val getTitle get() = title
    val getDescription get() = description
    val getPriority get() = priority

    constructor(parcel: Parcel) : this(
        parcel.readString()!!, // Title
        parcel.readString()!!, // Description
        parcel.readInt()!! // Priority
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeString(description)
        dest.writeInt(priority)
        dest.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(source: Parcel): Note {
            val note = Note(source)
            note.id = source.readInt() // Set Id
            return note
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}