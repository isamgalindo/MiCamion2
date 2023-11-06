package com.example.micamion2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar

class CreateTruckLocation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_truck_location)

        val progressbar = findViewById<ProgressBar>(R.id.progressBar)
        val currentprogress = 50
        progressbar.setProgress(currentprogress)
        progressbar.max = 100
        val buttonNext = findViewById<Button>(R.id.nextButton)
        buttonNext.setOnClickListener {
            val pickUpEditText = findViewById<EditText>(R.id.pickUpLocation)
            val dropOffEditText = findViewById<EditText>(R.id.destination)

            val pickUpLocation = pickUpEditText.text.toString()
            val dropOffLocation = dropOffEditText.text.toString()

            val sharedPreferences = getSharedPreferences("TruckDetails", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("pickUpLocation", pickUpLocation)
            editor.putString("dropOffLocation", dropOffLocation)
            editor.apply()

            val intent = Intent(this, CreateTruckDate::class.java)
            startActivity(intent)
        }
        val searchIcon = findViewById<ImageView>(R.id.searchIcon1)
        val searchIcon2 = findViewById<ImageView>(R.id.searchIcon2)

        searchIcon.setOnClickListener {
            val intent = Intent(this, FuncAF::class.java)
            startActivity(intent)
            // Handle the click on the icon here
            // You can open a search dialog or perform any action when the icon is clicked
        }

        searchIcon2.setOnClickListener {
            val intent = Intent(this, FuncAF::class.java)
            startActivity(intent)
            // Handle the click on the icon here
            // You can open a search dialog or perform any action when the icon is clicked
        }
    }
}