package amichaelpalmer.kotlin.noteappjetpack.view

import amichaelpalmer.kotlin.noteappjetpack.adapter.NoteAdapter
import amichaelpalmer.kotlin.noteappjetpack.data.Note
import amichaelpalmer.kotlin.noteappjetpack.viewmodel.NoteViewModel
import amichaelpalmer.kotlin.noteappjetpack.viewmodel.NoteViewModelFactory
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.jetpackarchitecturedemo.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainFragment : Fragment() {

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // We ask the system for a ViewModel; don't have to handle instance management
        noteViewModel = ViewModelProvider(
            requireActivity(),
            NoteViewModelFactory(
                requireActivity().application
            )
        ).get(NoteViewModel::class.java)

        // If there is a Note in the arguments, it means we've just come back from the AddEditNoteFragment and need to update the ViewModel
        arguments?.let {
            val safeArgs = MainFragmentArgs.fromBundle(it)
            val note = safeArgs.note
            if (note != null) {
                saveUpdateNoteInViewModel(note)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = "Notes"
        setHasOptionsMenu(true)

        val floatingActionButton: FloatingActionButton = view.findViewById(R.id.add_note_fab)
        floatingActionButton.setOnClickListener {
            Log.d(TAG, "floatingActionButton clicked")
            val action =
                MainFragmentDirections.actionMainFragmentToAddEditNoteFragment(null) // We aren't editing a note so we pass null
            view.findNavController().navigate(action)
        }
        val recyclerView: RecyclerView = view.findViewById(R.id.list_recycler_view)
        recyclerView.setHasFixedSize(true) // Efficiency, true if we know the RecyclerView size won't change
        val adapter = NoteAdapter()
        recyclerView.adapter = adapter

        noteViewModel.getNoteList().observe(viewLifecycleOwner, Observer {
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
                val action = MainFragmentDirections.actionMainFragmentToAddEditNoteFragment(note)
                view.findNavController().navigate(action)
            }
        })
    }

    private fun saveUpdateNoteInViewModel(note: Note) {
        noteViewModel.insert(note)
        this.arguments?.clear() // We can safely clear the arguments as it will only contain the note, which has now been added to the database
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all_notes -> {
                AlertDialog.Builder(requireContext())
                    .setTitle(R.string.delete_all_notes)
                    .setMessage(R.string.delete_all_notes_prompt)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(
                        android.R.string.yes
                    ) { _, _ ->
                        noteViewModel.deleteAllNotes()
                        Toast.makeText(activity, "All notes deleted", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton(android.R.string.no, null).show()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val TAG = "MainFragment"

    }
}