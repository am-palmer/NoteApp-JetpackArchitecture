package amichaelpalmer.kotlin.noteappjetpack.view

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import com.example.jetpackarchitecturedemo.R

// todo: hook up to the main fragment

class AddEditNoteFragment : Fragment() {
    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var numberPickerPriority: NumberPicker

    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        id = savedInstanceState?.getInt(BUNDLE_ID)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_add_edit_note, container
            , false
        )
        numberPickerPriority = view.findViewById(R.id.number_picker_priority)
        editTextTitle = view.findViewById(R.id.edit_text_title)
        editTextDescription = view.findViewById(R.id.edit_text_description)

        numberPickerPriority.minValue = 1
        numberPickerPriority.maxValue = 10

        // Check if we're editing a saved note
        if (savedInstanceState != null) {
            // todo: set title to "Edit"

            // Get fields from bundle and set them in the views
            editTextTitle.setText(savedInstanceState.getString(BUNDLE_TITLE, "Title"))
            editTextDescription.setText(
                savedInstanceState.getString(
                    BUNDLE_DESCRIPTION,
                    "Description"
                )
            )
            numberPickerPriority.value = savedInstanceState.getInt(BUNDLE_PRIORITY, 1)

        }

        return view
    }

    private fun saveNote() {

        val title = editTextTitle.text.toString()
        val description = editTextDescription.text.toString()
        val priority = numberPickerPriority.value

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            // todo: display toast message
//            Toast.makeText(
//                this,
//                "Please insert a title and description for the note",
//                Toast.LENGTH_SHORT
//            )
            return
        }

        // Make a bundle and add the new values
        val bundle = Bundle()
        bundle.putString(BUNDLE_TITLE, title)
        bundle.putString(BUNDLE_DESCRIPTION, description)
        bundle.putInt(BUNDLE_PRIORITY, priority)

        if (id != null) {
            bundle.putInt(BUNDLE_ID, id!!)
        }

        // todo: pass data back to the main fragment so it can be inserted in the list/room db

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

    companion object {
        const val BUNDLE_ID = "EXTRA_ID"
        const val BUNDLE_TITLE = "EXTRA_TITLE"
        const val BUNDLE_DESCRIPTION = "EXTRA_DESCRIPTION"
        const val BUNDLE_PRIORITY = "EXTRA_PRIORITY"
        const val INVALID_ID = -1
    }
}
