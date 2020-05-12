package amichaelpalmer.kotlin.noteappjetpack.view

import amichaelpalmer.kotlin.noteappjetpack.data.Note
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.jetpackarchitecturedemo.R

class AddEditNoteFragment : Fragment() {

    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var numberPickerPriority: NumberPicker

    private var note: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val safeArgs = AddEditNoteFragmentArgs.fromBundle(it)
            note = safeArgs.note // Null if we are creating a new note
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_add_edit_note, container
            , false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        numberPickerPriority = view.findViewById(R.id.number_picker_priority)
        editTextTitle = view.findViewById(R.id.edit_text_title)
        editTextDescription = view.findViewById(R.id.edit_text_description)
        numberPickerPriority.minValue = 1
        numberPickerPriority.maxValue = 10

        // Check if we're editing a saved note
        if (note != null) { // We're editing an existing note
            requireActivity().title = "Edit"
            // Get fields from bundle and set them in the views
            editTextTitle.setText(note?.getTitle)
            editTextDescription.setText(
                note?.getDescription
            )
            numberPickerPriority.value = note?.getPriority ?: 1

        } else { // We're creating a new note
            requireActivity().title = "New"
            numberPickerPriority.value = 1 // May be unneeded
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_note_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_note -> {
                saveNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveNote() {
        val title = editTextTitle.text.toString()
        val description = editTextDescription.text.toString()
        val priority = numberPickerPriority.value

        if (title.trim().isEmpty()) {
            Toast.makeText(requireActivity(), "Title required", Toast.LENGTH_SHORT).show()
            return
        } else if (description.trim().isEmpty()) {
            Toast.makeText(requireActivity(), "Description required", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val newNote = Note(title, description, priority)

        if (note != null) {
            // If note currently exists, get the ID so we can replace it in Room
            newNote.id = note!!.id
            Toast.makeText(requireContext(), "Note updated", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Note saved", Toast.LENGTH_SHORT).show()
        }

        val action = AddEditNoteFragmentDirections.actionAddEditNoteFragmentToMainFragment(newNote)
        requireView().findNavController()
            .navigate(action)
    }

    companion object {
        private const val TAG = "AddEditNoteFragment"
    }
}
