package com.example.micamion2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileDriver : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_driver)

        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val name = sharedPref.getString("name", "DefaultName")
        val userType = sharedPref.getString("userType", "DefaultUserType")
        val email = sharedPref.getString("email", "DefaultEmail")
        val phone = sharedPref.getString("phone", "DefaultPhone")
        val lastName = sharedPref.getString("lastName", "DefaultLastName")


        // Get the TextView reference
        val nameProfileTextView: TextView = findViewById(R.id.nameProfile)
        val roleProfileTextView: TextView = findViewById(R.id.roleProfile)
        val emailProfileTextView: TextView = findViewById(R.id.emailProfile)
        val phoneTypeProfileTextView: TextView = findViewById(R.id.phoneProfile)
        // Set the text using the string resource with placeholder
        nameProfileTextView.text = "$name $lastName"

        emailProfileTextView.text = email
        phoneTypeProfileTextView.text = phone

        if (userType == "LO"){
            roleProfileTextView.text = "Load Owner"
        }
        if (userType == "TO"){
            roleProfileTextView.text = "Truck Owner"

        }
        if (userType == "DR"){
            roleProfileTextView.text = "Driver"
        }

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.profile
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profile -> {
                    true
                }
                R.id.history->{
                    val intent = Intent(this@ProfileDriver, HistoryView::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.home ->{
                    val intent = Intent(this@ProfileDriver, ServicesDriver::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                // Add other menu items here...
                else -> false
            }
        }

        val logOutButton = findViewById<Button>(R.id.logOut)
        logOutButton.setOnClickListener {
            clearSharedPreferences()
            logOut()
        }


    }

    private fun clearSharedPreferences() {
        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply() // or editor.commit() for synchronous removal
    }

    private fun logOut() {
        val intent = Intent(this, LoginPage::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // This will finish the current activity
    }
}