package amichaelpalmer.kotlin.noteappjetpack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.jetpackarchitecturedemo.R

class MainActivity : AppCompatActivity() {
    private var noteViewModel: NoteViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // We ask the system for a ViewModel; don't have to handle instance management
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel?.getAllNotes?.observe(this, Observer {
            Toast.makeText(this, "Observer onchanged!", Toast.LENGTH_SHORT).show()
        })
    }
}
