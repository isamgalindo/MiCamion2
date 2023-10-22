package com.example.micamion2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import android.view.View


class LikeToDo : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like_to_do)

        // Find the CardView by its ID
        val loadOwner = findViewById<CardView>(R.id.LoadOwner)
        val driver = findViewById<CardView>(R.id.Driver)
        val trailerOwner = findViewById<CardView>(R.id.TrailerOwner)

        // Set an OnClickListener
        loadOwner.setOnClickListener {
            val intent = Intent(this, CreateAccount::class.java)
            // If you need to pass data to the new view, you can use intent.putExtra()
            intent.putExtra("User Type", "LO")
            startActivity(intent)
        }
        driver.setOnClickListener {
            val intent = Intent(this, CreateAccount::class.java)
            // If you need to pass data to the new view, you can use intent.putExtra()
            intent.putExtra("User Type", "DR")
            startActivity(intent)
        }
        trailerOwner.setOnClickListener {
            val intent = Intent(this, CreateAccount::class.java)
            // If you need to pass data to the new view, you can use intent.putExtra()
            intent.putExtra("User Type", "TO")
            startActivity(intent)
        }
    }
}