package com.example.micamion2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.micamion2.databinding.ActivityMainBinding
import android.text.method.LinkMovementMethod
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import java.time.LocalTime


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var logButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Check if the current time is after 6pm
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


    }
}