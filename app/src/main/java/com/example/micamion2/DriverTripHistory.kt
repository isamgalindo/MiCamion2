package com.example.micamion2

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent


class DriverTripHistory: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_truck)

        val buttonLogin = findViewById<Button>(R.id.triphistorybutton)
        buttonLogin.setOnClickListener {
            val intent = Intent(this, DriverTripHistory::class.java)
            startActivity(intent)
        }

    }
}