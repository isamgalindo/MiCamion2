package com.example.micamion2

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent

class AdminTrucks: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_trucks)

        val buttonLogin = findViewById<Button>(R.id.triphistorybutton2
        )
        buttonLogin.setOnClickListener {
            val intent = Intent(this, AdminTrucks::class.java)
            startActivity(intent)
        }

    }

}