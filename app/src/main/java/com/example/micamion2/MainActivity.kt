package com.example.micamion2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.micamion2.databinding.ActivityMainBinding
import java.time.LocalTime


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var logButton: Button
    private lateinit var viewModel: DarkModeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*

        viewModel = ViewModelProvider(this)[DarkModeViewModel::class.java]

        viewModel.currentTheme.observe(this) { isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            // Since we're changing the theme, we'll need to recreate the activity
            recreate()
        }
        // Check if the current time is after 6pm

         */

        val currentTime = LocalTime.now()
        if (currentTime.isAfter(LocalTime.of(18, 0))) {
            // Set to dark mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            // Set to light mode (or however you'd like to handle it)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }


        setContentView(R.layout.activity_main)


        val buttonClick = findViewById<Button>(R.id.logButton)
        buttonClick.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
            }

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this){
            if (it){
                Toast.makeText(this, "Connected to Internet", Toast.LENGTH_SHORT).show()
                setContentView(R.layout.activity_main)
            } else {
                Toast.makeText(this, "Not connected to Internet", Toast.LENGTH_SHORT).show()
                setContentView(R.layout.activity_network_error_layout)
            }
        }
    }
}