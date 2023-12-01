package com.example.micamion2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ServicesDriver : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services_driver)

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
            finish()
            }

        val cardViewCurrentTrip = findViewById<CardView>(R.id.currentTrip)

            // Set an OnClickListener
        cardViewCurrentTrip.setOnClickListener {
                val intent = Intent(this, CurrentTripDriver::class.java)
                startActivity(intent)
            }

        }

}