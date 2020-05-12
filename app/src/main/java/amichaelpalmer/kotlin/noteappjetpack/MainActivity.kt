package amichaelpalmer.kotlin.noteappjetpack

import amichaelpalmer.kotlin.noteappjetpack.viewmodel.NoteViewModel
import amichaelpalmer.kotlin.noteappjetpack.viewmodel.NoteViewModelFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.jetpackarchitecturedemo.R

// todo: finish writing fragments, then implement navigation graph with safeargs
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // We ask the system for a ViewModel; don't have to handle instance management
        val noteViewModel = ViewModelProvider(
            this,
            NoteViewModelFactory(
                application
            )
        ).get(NoteViewModel::class.java)

        // todo: launch mainfragment and pass viewmodel


    }
}
