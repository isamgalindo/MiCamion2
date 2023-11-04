package com.example.micamion2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileView : AppCompatActivity() {
    private var name = ""
    private var email =""
    private var userType =""
    private var lastName =""
    private var phone =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_view)

        val intent = intent
        name = intent.getStringExtra("Name").toString()
        userType = intent.getStringExtra("User Type").toString()
        email = intent.getStringExtra("Email").toString()
        phone= intent.getStringExtra("Phone").toString()
        lastName= intent.getStringExtra("Last Name").toString()

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
                R.id.loadmenu ->{
                    val intent = Intent(this@ProfileView, SendView::class.java)
                    intent.putExtra("Name", name)
                    intent.putExtra("Email", email)
                    intent.putExtra("User Type", userType)
                    intent.putExtra("Last Name", lastName)
                    intent.putExtra("Phone", phone)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.home ->{
                    val intent = Intent(this@ProfileView, ServicesCompanyPersona::class.java)
                    intent.putExtra("Name", name)
                    intent.putExtra("Email", email)
                    intent.putExtra("User Type", userType)
                    intent.putExtra("Last Name", lastName)
                    intent.putExtra("Phone", phone)
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