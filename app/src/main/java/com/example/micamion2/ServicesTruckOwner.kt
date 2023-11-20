package com.example.micamion2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class ServicesTruckOwner : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services_truck_owner)

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