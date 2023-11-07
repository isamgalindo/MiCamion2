package com.example.micamion2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView

class LoadCompleteDetailsView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_complete_details_view)
        val sharedPref = getSharedPreferences("LoadDetails", Context.MODE_PRIVATE)

        val name = sharedPref.getString("nameLoad", "") ?: ""
        val type = sharedPref.getString("typeLoad", "") ?: ""
        val weight = sharedPref.getString("weightLoad", "") ?: ""
        val volume = sharedPref.getString("volumeLoad", "") ?: ""
        val pickUp = sharedPref.getString("pickUpLocation", "") ?: ""
        val dropOff = sharedPref.getString("dropOffLocation", "") ?: ""
        val recipientName = sharedPref.getString("recipientName", "") ?: ""
        val recipientPhone = sharedPref.getString("recipientPhone", "") ?: ""
        val pickUpDate = sharedPref.getString("pickUpDate", "") ?: ""
        val dropOffDate = sharedPref.getString("dropOffDate", "") ?: ""

        // Get the TextView reference
        val loadNameTextView: TextView = findViewById(R.id.loadNameReview)
        val loadTypeTextView: TextView = findViewById(R.id.loadTypeReview)
        val loadWeightTextView: TextView = findViewById(R.id.loadWeightReview)
        val loadVolumeTextView: TextView = findViewById(R.id.loadVolumeReview)
        val loadpickUpTextView: TextView = findViewById(R.id.loadPickUpReview)
        val loadDropOffTextView: TextView = findViewById(R.id.loadDropOffReview)
        val recipientNameTextView: TextView = findViewById(R.id.recipientNameReview)
        val recipientPhoneTextView: TextView = findViewById(R.id.phoneNumber)
        val loadpickUpDateTextView: TextView = findViewById(R.id.pickUpDateReview)
        val loadDropOffDateTextView: TextView = findViewById(R.id.dropoffDate)
        // Set the text using the string resource with placeholder

        loadNameTextView.text = name
        loadTypeTextView.text = type
        loadWeightTextView.text = weight
        loadVolumeTextView.text = volume
        loadpickUpTextView.text = pickUp
        loadDropOffTextView.text = dropOff
        recipientNameTextView.text = recipientName
        recipientPhoneTextView.text = recipientPhone
        loadpickUpDateTextView.text = pickUpDate
        loadDropOffDateTextView.text = dropOffDate

        val buttonOK = findViewById<Button>(R.id.okButton)
        buttonOK.setOnClickListener {
            val intent = Intent(this, SendView::class.java)
            startActivity(intent)
        }


    }
}