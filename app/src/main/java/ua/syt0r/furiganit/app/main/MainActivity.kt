package ua.syt0r.furiganit.app.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ua.syt0r.furiganit.R

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)
        toolbar.setupWithNavController(navController)
        toolbar.setOnMenuItemClickListener {
            navController.navigate(it.itemId)
            true
        }

        viewModel.tryToShowReviewFlow(this)
    }

}
