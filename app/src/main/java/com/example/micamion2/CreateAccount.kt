package com.example.micamion2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class CreateAccount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        val buttonSignUp = findViewById<Button>(R.id.SignUpButton)
        buttonSignUp.setOnClickListener {
            val intent = Intent(this, LikeToDo::class.java)
            startActivity(intent)
        }
    }

}