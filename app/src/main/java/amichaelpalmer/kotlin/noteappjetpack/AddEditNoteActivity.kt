package amichaelpalmer.kotlin.noteappjetpack

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import com.example.jetpackarchitecturedemo.R

// todo convert to fragment

class AddEditNoteActivity : AppCompatActivity() {
    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var numberPickerPriority: NumberPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        numberPickerPriority = findViewById(R.id.number_picker_priority)
        editTextTitle = findViewById(R.id.edit_text_title)
        editTextDescription = findViewById(R.id.edit_text_description)

        numberPickerPriority.minValue = 1
        numberPickerPriority.maxValue = 10

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
        // Check whether we are editing an existing note or creating a new one
        if (intent.hasExtra(EXTRA_ID)) {
            title = "Edit Note"
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE))
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            numberPickerPriority.value = intent.getIntExtra(EXTRA_PRIORITY, 1)
        } else {
            title = "Add Note"
        }
    }

    private fun saveNote() {
        val title = editTextTitle.text.toString()
        val description = editTextDescription.text.toString()
        val priority = numberPickerPriority.value

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(
                this,
                "Please insert a title and description for the note",
                Toast.LENGTH_SHORT
            )
            return
        }

        val data = Intent()

        data.putExtra(EXTRA_TITLE, title)
        data.putExtra(EXTRA_DESCRIPTION, description)
        data.putExtra(EXTRA_PRIORITY, priority)

        // Add the primary key if it exists
        val id = intent.getIntExtra(EXTRA_ID, INVALID_ID)
        if (id != INVALID_ID) {
            data.putExtra(EXTRA_ID, id)
        }

        setResult(Activity.RESULT_OK, data)
        finish()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
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
        const val EXTRA_ID = "amichaelpalmer.kotlin.noteappjetpack.EXTRA_ID"
        const val EXTRA_TITLE = "amichaelpalmer.kotlin.noteappjetpack.EXTRA_TITLE"
        const val EXTRA_DESCRIPTION = "amichaelpalmer.kotlin.noteappjetpack.EXTRA_DESCRIPTION"
        const val EXTRA_PRIORITY = "amichaelpalmer.kotlin.noteappjetpack.EXTRA_PRIORITY"
        const val INVALID_ID = -1
    }
}
