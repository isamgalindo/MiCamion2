package com.example.micamion2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ServicesDriver : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {




        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services_driver)

        val sharedPref = getSharedPreferences("LoadDetails", Context.MODE_PRIVATE)
        val name = sharedPref.getString("name", "DefaultName")

        // Get the TextView reference
        val textView: TextView = findViewById(R.id.welcome)
        textView.text = "Welcome $name!"

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.home
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    true
                }

                R.id.history -> {
                    val intent = Intent(this@ServicesDriver, HistoryView::class.java)
                    startActivity(intent)
                    finish()
                    true
                }

                R.id.profile -> {
                    val intent = Intent(this@ServicesDriver, ProfileDriver::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                // Add other menu items here...
                else -> false
            }
        }

        val cardViewHistory = findViewById<CardView>(R.id.historyCard)

        // Set an OnClickListener
        cardViewHistory.setOnClickListener {
            val intent = Intent(this, AssignTrailerDriver::class.java)
            startActivity(intent)
            }

        val cardViewCurrentTrip = findViewById<CardView>(R.id.currentTrip)

            // Set an OnClickListener
        cardViewCurrentTrip.setOnClickListener {
                val intent = Intent(this, CurrentTripDriver::class.java)
                startActivity(intent)
            }

        }

}