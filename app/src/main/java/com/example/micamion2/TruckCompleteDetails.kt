package com.example.micamion2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class TruckCompleteDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_truck_complete_details)

        val sharedPref = getSharedPreferences("TruckDetails", Context.MODE_PRIVATE)

        val model = sharedPref.getString("modelTruck", "") ?: ""
        val brand = sharedPref.getString("brandTruck", "") ?: ""
        val plates = sharedPref.getString("platesTruck", "") ?: ""
        val weight = sharedPref.getString("weightTruck", "") ?: ""
        val volume = sharedPref.getString("volumeTruck", "") ?: ""
        val driver = sharedPref.getString("driverTruck", "") ?: ""
        val pickUp = sharedPref.getString("pickUpLocation", "") ?: ""
        val dropOff = sharedPref.getString("dropOffLocation", "") ?: ""

        // Get the TextView reference
        val modelTextView: TextView = findViewById(R.id.modelReview)
        val driverTextView: TextView = findViewById(R.id.driverReview)
        val weightTextView: TextView = findViewById(R.id.weightReview)
        val volumeTextView: TextView = findViewById(R.id.volumeReview)
        val pickUpTextView: TextView = findViewById(R.id.truckPickUpReview)
        val dropOffTextView: TextView = findViewById(R.id.truckDropOffReview)
        //val loadpickUpDateTextView: TextView = findViewById(R.id.pickUpDateReview)
        //val loadDropOffDateTextView: TextView = findViewById(R.id.loadDropOffReview)
        // Set the text using the string resource with placeholder

        modelTextView.text = "$brand $model $plates"
        driverTextView.text = driver
        weightTextView.text = weight
        volumeTextView.text = volume
        pickUpTextView.text = pickUp
        dropOffTextView.text = dropOff

        val buttonOK = findViewById<Button>(R.id.okButton)
        buttonOK.setOnClickListener {
            val intent = Intent(this, TrucksView::class.java)
            startActivity(intent)
        }
    }
}