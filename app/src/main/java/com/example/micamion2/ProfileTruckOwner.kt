package com.example.micamion2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileTruckOwner : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_truck_owner)

        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val name = sharedPref.getString("name", "DefaultName")
        val userType = sharedPref.getString("userType", "DefaultUserType")
        val email = sharedPref.getString("email", "DefaultEmail")
        val phone = sharedPref.getString("phone", "DefaultPhone")


        // Get the TextView reference
        val nameProfileTextView: TextView = findViewById(R.id.nameProfile)
        val roleProfileTextView: TextView = findViewById(R.id.roleProfile)
        val emailProfileTextView: TextView = findViewById(R.id.emailProfile)
        val phoneTypeProfileTextView: TextView = findViewById(R.id.phoneProfile)
        // Set the text using the string resource with placeholder
        nameProfileTextView.text = name
        roleProfileTextView.text = userType
        emailProfileTextView.text = email
        phoneTypeProfileTextView.text = phone

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.profile
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profile -> {
                    true
                }
                R.id.truckMenu->{
                    val intent = Intent(this@ProfileTruckOwner, TrucksView::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.home ->{
                    val intent = Intent(this@ProfileTruckOwner, ServicesTruckOwner::class.java)
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