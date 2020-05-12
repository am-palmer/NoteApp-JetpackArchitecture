package amichaelpalmer.kotlin.noteappjetpack.data

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

// We have to define the entities ('tables') we want the database to contain with the @Database annotation. Version is usually incremented when changes are made in production
@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        private var instance: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase? {
            if (instance == null) {
                synchronized(NoteDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NoteDatabase::class.java, "note_database"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build()
                }
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }

        // Populates the database with some notes on creation
        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(sqLiteDatabase: SupportSQLiteDatabase) {
                super.onCreate(sqLiteDatabase)
                PopulateDbAsyncTask(
                    instance
                )
                    .execute()
            }
        }

    }

    private class PopulateDbAsyncTask(noteDatabase: NoteDatabase?) : AsyncTask<Unit, Unit, Unit>() {
        private val noteDao = noteDatabase?.noteDao()

        override fun doInBackground(vararg p0: Unit?) {
            // Insert some placeholder notes into the database
            noteDao?.insertOrUpdate(
                Note(
                    "Title 1",
                    "Description 1",
                    1
                )
            )
            noteDao?.insertOrUpdate(
                Note(
                    "Title 2",
                    "Description 2",
                    2
                )
            )
            noteDao?.insertOrUpdate(
                Note(
                    "Title 3",
                    "Description 3",
                    3
                )
            )
        }
    }

}