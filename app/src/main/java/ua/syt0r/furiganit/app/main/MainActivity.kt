package ua.syt0r.furiganit.app.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import ua.syt0r.furiganit.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)
        toolbar.setupWithNavController(navController)
        toolbar.setOnMenuItemClickListener {
            navController.navigate(it.itemId)
            true
        }
    }

}
