package amichaelpalmer.kotlin.noteappjetpack.view

import amichaelpalmer.kotlin.noteappjetpack.adapter.NoteAdapter
import amichaelpalmer.kotlin.noteappjetpack.data.Note
import amichaelpalmer.kotlin.noteappjetpack.viewmodel.NoteViewModel
import amichaelpalmer.kotlin.noteappjetpack.viewmodel.NoteViewModelFactory
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.jetpackarchitecturedemo.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainFragment(private val noteViewModel: NoteViewModel) : Fragment() {
    // todo: move logic from mainactivity to this fragment, then hook up the addeditnotefragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val floatingActionButton: FloatingActionButton = view.findViewById(R.id.add_note_fab)
        floatingActionButton.setOnClickListener {
            // todo: nav action here
//            val intent = Intent(this, AddEditNoteFragment::class.java)
//            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }
        val recyclerView: RecyclerView = view.findViewById(R.id.list_recycler_view)
        recyclerView.setHasFixedSize(true) // Efficiency, true if we know the recyclerview size won't change
        val adapter = NoteAdapter()
        recyclerView.adapter = adapter

        noteViewModel.getNoteList().observe(this, Observer {
            // Triggered every time there is a change to the LiveData
            adapter.submitList(it)
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
                Toast.makeText(activity, "Note deleted", Toast.LENGTH_SHORT).show()
            }
        }
        ).attachToRecyclerView(recyclerView)

        adapter.setOnItemLongTapListener(object : NoteAdapter.OnItemLongTapListener {
            override fun onItemLongTap(note: Note) {
                // todo: make a navigation graph, use safeargs to open edit fragment
//                val intent = Intent(this@MainActivity, AddEditNoteFragment::class.java)
//                // Pass the primary key so Room knows which note to update
//                intent.putExtra(AddEditNoteFragment.BUNDLE_ID, note.id)
//                intent.putExtra(AddEditNoteFragment.BUNDLE_TITLE, note.getTitle)
//                intent.putExtra(AddEditNoteFragment.BUNDLE_PRIORITY, note.getPriority)
//                intent.putExtra(AddEditNoteFragment.BUNDLE_DESCRIPTION, note.getDescription)
//
//                startActivityForResult(intent, EDIT_NOTE_REQUEST)

            }
        })

        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val title = data?.getStringExtra(AddEditNoteFragment.BUNDLE_TITLE) ?: "Title"
            val description =
                data?.getStringExtra(AddEditNoteFragment.BUNDLE_DESCRIPTION) ?: "Description"
            val priority = data?.getIntExtra(AddEditNoteFragment.BUNDLE_PRIORITY, 1) ?: 1

            val note = Note(title, description, priority)
            noteViewModel.insert(note)
            Toast.makeText(activity, "Note saved", Toast.LENGTH_SHORT).show()
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            // We retrieve the ID (primary key) of the note being edited
            val id =
                data?.getIntExtra(AddEditNoteFragment.BUNDLE_ID, AddEditNoteFragment.INVALID_ID)
            if (id == AddEditNoteFragment.INVALID_ID) {
                Log.e(TAG, ".onActivityResult: Error passing value from AddEditNoteFragment")
                return
            }
            val title = data?.getStringExtra(AddEditNoteFragment.BUNDLE_TITLE) ?: "Title"
            val description =
                data?.getStringExtra(AddEditNoteFragment.BUNDLE_DESCRIPTION) ?: "Description"
            val priority = data?.getIntExtra(AddEditNoteFragment.BUNDLE_PRIORITY, 1) ?: 1

            val note = Note(title, description, priority)
            note.id = id!!
            noteViewModel.update(note)
            Toast.makeText(activity, "Note updated", Toast.LENGTH_SHORT)

        } else {
            // Cancelled
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all_notes -> {
                noteViewModel.deleteAllNotes()
                Toast.makeText(activity, "All notes deleted", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val TAG = "MainFragment"
        const val ADD_NOTE_REQUEST = 1
        const val EDIT_NOTE_REQUEST = 2
    }
}