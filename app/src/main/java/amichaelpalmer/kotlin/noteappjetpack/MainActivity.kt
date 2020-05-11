package amichaelpalmer.kotlin.noteappjetpack

import amichaelpalmer.kotlin.noteappjetpack.data.Note
import amichaelpalmer.kotlin.noteappjetpack.viewmodel.NoteViewModel
import amichaelpalmer.kotlin.noteappjetpack.viewmodel.NoteViewModelFactory
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.jetpackarchitecturedemo.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val floatingActionButton: FloatingActionButton = findViewById(R.id.add_note_fab)
        floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddEditNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }

        val recyclerView: RecyclerView = findViewById(R.id.list_recycler_view)
        recyclerView.setHasFixedSize(true) // Efficiency, true if we know the recyclerview size won't change
        val adapter = NoteAdapter()
        recyclerView.adapter = adapter
        // We ask the system for a ViewModel; don't have to handle instance management
        noteViewModel = ViewModelProvider(
            this,
            NoteViewModelFactory(
                application
            )
        ).get(NoteViewModel::class.java)

        noteViewModel.getNoteList().observe(this, Observer<List<Note>> {
            // Triggered every time there is a change to the LiveData
            adapter.setNotes(it)
        })
        // Set up deletion by swiping
        val swipeDirs = ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, swipeDirs) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.delete(adapter.getNoteAtPosition(viewHolder.adapterPosition))
                Toast.makeText(baseContext, "Note deleted", Toast.LENGTH_SHORT).show()
            }
        }
        ).attachToRecyclerView(recyclerView)

        adapter.setOnItemLongTapListener(object : NoteAdapter.OnItemLongTapListener {
            override fun onItemLongTap(note: Note) {
                val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
                // Pass the primary key so Room knows which note to update
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.id)
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle)
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority)
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription)

                startActivityForResult(intent, EDIT_NOTE_REQUEST)

            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val title = data?.getStringExtra(AddEditNoteActivity.EXTRA_TITLE) ?: "Title"
            val description =
                data?.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION) ?: "Description"
            val priority = data?.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1) ?: 1

            val note = Note(title, description, priority)
            noteViewModel.insert(note)
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            // We retrieve the ID (primary key) of the note being edited
            val id = data?.getIntExtra(AddEditNoteActivity.EXTRA_ID, AddEditNoteActivity.INVALID_ID)
            if (id == AddEditNoteActivity.INVALID_ID) {
                Log.e(TAG, ".onActivityResult: Error passing value from AddEditNoteActivity")
                return
            }
            val title = data?.getStringExtra(AddEditNoteActivity.EXTRA_TITLE) ?: "Title"
            val description =
                data?.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION) ?: "Description"
            val priority = data?.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1) ?: 1

            val note = Note(title, description, priority)
            note.id = id!!
            noteViewModel.update(note)
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT)

        } else {
            // Cancelled
        }
    }

    companion object {
        const val TAG = "MainActivity"
        const val ADD_NOTE_REQUEST = 1
        const val EDIT_NOTE_REQUEST = 2
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all_notes -> {
                noteViewModel.deleteAllNotes()
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
