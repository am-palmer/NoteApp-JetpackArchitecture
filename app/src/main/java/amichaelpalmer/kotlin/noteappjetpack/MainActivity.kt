package amichaelpalmer.kotlin.noteappjetpack

import amichaelpalmer.kotlin.noteappjetpack.data.Note
import amichaelpalmer.kotlin.noteappjetpack.viewmodel.NoteViewModel
import amichaelpalmer.kotlin.noteappjetpack.viewmodel.NoteViewModelFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jetpackarchitecturedemo.R

class MainActivity : AppCompatActivity() {

    // We ask the system for a ViewModel; don't have to handle instance management
    private val noteViewModel by lazy {
        ViewModelProvider(
            this,
            NoteViewModelFactory(
                application
            )
        ).get(NoteViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.list_recycler_view)
       // recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true) // Efficiency, true if we know the recyclerview size won't change
        val adapter = NoteAdapter()
        recyclerView.adapter = adapter

        noteViewModel.getNoteList().observe(this, Observer<List<Note>> {
            // Triggered every time there is a change to the LiveData
            adapter.setNotes(it)
        })
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
