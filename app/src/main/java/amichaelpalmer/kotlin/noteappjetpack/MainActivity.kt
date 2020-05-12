package amichaelpalmer.kotlin.noteappjetpack

import amichaelpalmer.kotlin.noteappjetpack.viewmodel.NoteViewModel
import amichaelpalmer.kotlin.noteappjetpack.viewmodel.NoteViewModelFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.jetpackarchitecturedemo.R

// todo: finish writing fragments, then implement navigation graph with safeargs
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // todo: launch mainfragment and pass viewmodel

        //var navController = findNavController(R.id.activity_nav_host_fragment)


    }
}
