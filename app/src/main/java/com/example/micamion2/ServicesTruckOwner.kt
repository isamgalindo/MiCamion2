package com.example.micamion2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ServicesTruckOwner : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services_truck_owner)

        val sharedPref = getSharedPreferences("LoadDetails", Context.MODE_PRIVATE)
        val name = sharedPref.getString("name", "DefaultName")

        // Get the TextView reference
        val textView: TextView = findViewById(R.id.welcome)
        textView.text = "Welcome $name!"

        val addTruckCard = findViewById<CardView>(R.id.addTruck)
        val manageTrucksCard = findViewById<CardView>(R.id.manageTrucks)
        addTruckCard.setOnClickListener {
            val intent = Intent(this@ServicesTruckOwner, CreateTruckDetails::class.java)
            startActivity(intent)
        }

        manageTrucksCard.setOnClickListener {
            val intent = Intent(this@ServicesTruckOwner, TrucksView::class.java)
            startActivity(intent)
        }

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.home
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    true
                }
                R.id.truckMenu ->{
                    val intent = Intent(this@ServicesTruckOwner, TrucksView::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.profile ->{
                    val intent = Intent(this@ServicesTruckOwner, ProfileTruckOwner::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                // Add other menu items here...
                else -> false
            }
        }
    }
}