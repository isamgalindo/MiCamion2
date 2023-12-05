package com.example.micamion2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ServicesCompanyPersona : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {





        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services_company_persona)

        val sharedPref = getSharedPreferences("LoadDetails", Context.MODE_PRIVATE)
        val name = sharedPref.getString("name", "DefaultName")
        val intent = intent

        val email = intent.getStringExtra("Email").toString()

        val userType = intent.getStringExtra("User Type").toString()
        val lastName = intent.getStringExtra("Last Name").toString()
        val phone = intent.getStringExtra("Phone").toString()

        // Get the TextView reference
        val textView: TextView = findViewById(R.id.welcome)
        textView.text = "Welcome $name!"
        // Set the text using the string resource with placeholder

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.home
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    true
                }
                R.id.loadmenu ->{
                    val intent = Intent(this@ServicesCompanyPersona, SendView::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.profile ->{
                    val intent = Intent(this@ServicesCompanyPersona, ProfileView::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                // Add other menu items here...
                else -> false
            }
        }




        val cardView = findViewById<CardView>(R.id.Send)

        // Set an OnClickListener
        cardView.setOnClickListener {
            val intent = Intent(this, SendView::class.java)
            intent.putExtra("Name", name)
            intent.putExtra("Email", email)
            intent.putExtra("User Type", userType)
            intent.putExtra("Last Name", lastName)
            intent.putExtra("Phone", phone)
            startActivity(intent)
            finish()
        }



    }
}