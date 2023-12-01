package com.example.micamion2

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import java.io.IOException
import java.util.Locale

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
        val pickUpEditText = findViewById<EditText>(R.id.pickUpLocation)
        val dropOffEditText = findViewById<EditText>(R.id.destination)

        fun getAddressFromLocation(latitude: Double, longitude: Double, editText: EditText) {
            val geocoder = Geocoder(this, Locale.getDefault())
            try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                addresses?.let {
                    if (it.isNotEmpty()) {
                        val address = it[0]
                        val addressFragments = (0..address.maxAddressLineIndex).map(address::getAddressLine)

                        // Combine the address fragments into a single string
                        val fullAddress = addressFragments.joinToString(separator = "\n")

                        // Now you have a full address string, you can set it to your EditText
                        runOnUiThread {
                            editText.setText(fullAddress)
                        }
                    } else {
                        // Handle case where no address was found.
                        runOnUiThread {
                            Toast.makeText(this, "No address found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: IOException) {
                // Handle the IO exception
                runOnUiThread {
                    Toast.makeText(this, "Geocoder service not available", Toast.LENGTH_SHORT).show()
                }
            }
        }


        searchIcon.setOnClickListener {
            val intent = Intent(this, OriginMap::class.java)
            startActivity(intent)
            val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
            val latitude = sharedPreferences.getFloat("location_lat", 0f)
            val longitude = sharedPreferences.getFloat("location_lng", 0f)
            // Check if we actually have a location saved (i.e., not 0f for both)
            if (latitude.toDouble() != 0.0 && longitude.toDouble() != 0.0) {
                getAddressFromLocation(latitude.toDouble(), longitude.toDouble(), pickUpEditText)
            } else {
                Toast.makeText(this, "No location saved", Toast.LENGTH_SHORT).show()
            }
        }

        searchIcon2.setOnClickListener {
            val intent = Intent(this, OriginMap::class.java)
            startActivity(intent)
            val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
            val latitude = sharedPreferences.getFloat("location_lat", 0f)
            val longitude = sharedPreferences.getFloat("location_lng", 0f)
            // Check if we actually have a location saved (i.e., not 0f for both)
            if (latitude.toDouble() != 0.0 && longitude.toDouble() != 0.0) {
                getAddressFromLocation(latitude.toDouble(), longitude.toDouble(), dropOffEditText)
            } else {
                Toast.makeText(this, "No location saved", Toast.LENGTH_SHORT).show()
            }
        }
    }
}