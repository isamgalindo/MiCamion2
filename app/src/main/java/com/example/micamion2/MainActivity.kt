package com.example.micamion2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.micamion2.databinding.ActivityMainBinding
import android.text.method.LinkMovementMethod
import android.content.Intent



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var logButton: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonClick = findViewById<Button>(R.id.logButton)
        buttonClick.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
            }


        val buttonClick2 = findViewById<Button>(R.id.botonMapa)
        buttonClick2.setOnClickListener {
            val intent = Intent(this, FuncAF::class.java)
            startActivity(intent)
        }

    }
}