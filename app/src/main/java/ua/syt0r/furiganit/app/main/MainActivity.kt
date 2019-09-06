package ua.syt0r.furiganit.app.main

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import ua.syt0r.furiganit.R

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        NavigationUI.setupWithNavController(toolbar, navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        navController.navigate(item.itemId)
        return super.onOptionsItemSelected(item)
    }

}
