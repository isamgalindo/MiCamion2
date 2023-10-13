package com.example.micamion2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView

class ServicesCompanyPersona : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services_company_persona)

        val cardView = findViewById<CardView>(R.id.Send)

        // Set an OnClickListener
        cardView.setOnClickListener {
            val intent = Intent(this, SendView::class.java)
            // If you need to pass data to the new view, you can use intent.putExtra()
            // intent.putExtra("key", value)
            startActivity(intent)
        }
    }
}