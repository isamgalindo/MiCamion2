package com.example.micamion2

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

class HistoryTripsDetailed : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var pickUpLocation: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_trips_detailed)

        val backButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        // Set an OnClickListener on the button
        backButton.setOnClickListener {
            // Finish the current activity
            finish()
        }

        val address = intent.getStringExtra("pickUpAddress")
        val city = intent.getStringExtra("pickUpCity")
        val country = intent.getStringExtra("pickUpCountry")
        val dropOffAddress = intent.getStringExtra("dropOffAddress")
        val dropOffCity = intent.getStringExtra("dropOffCity")
        val dropOffCountry = intent.getStringExtra("dropOffCountry")
        val loadType = intent.getStringExtra("loadType")
        val loadWeight = intent.getStringExtra("loadWeight")
        val pickUpDate = intent.getStringExtra("pickUpDate")
        val dropOffDate = intent.getStringExtra("dropOffDate")


        val loadTypeTextView: TextView = findViewById(R.id.loadTypeDetail)
        val loadWeightTextView: TextView = findViewById(R.id.weightDetail)
        val loadpickUpTextView: TextView = findViewById(R.id.pickUpLocationDetail)
        val loadDropOffTextView: TextView = findViewById(R.id.dropOffLocationDetail)
        val pickUpDateTextView: TextView = findViewById(R.id.pickUpDateDetail)
        val dropOffDateTextView: TextView = findViewById(R.id.dropOffDateDetail)
        // Set the text using the string resource with placeholder

        loadTypeTextView.text = loadType
        loadWeightTextView.text = loadWeight
        loadpickUpTextView.text = "${address}, ${city}, $country"
        loadDropOffTextView.text = "${dropOffAddress}, ${dropOffCity}, $dropOffCountry"
        pickUpDateTextView.text = pickUpDate
        dropOffDateTextView.text = dropOffDate



        pickUpLocation = "${address}, ${city}, $country" // Replace with actual address

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setUpMap()
    }

    private fun setUpMap() {
        if (pickUpLocation.isNotBlank()) {
            val geocoder = Geocoder(this)
            try {
                val addressList = geocoder.getFromLocationName(pickUpLocation, 1)
                if (addressList != null) {
                    if (addressList.isNotEmpty()) {
                        val address = addressList[0]
                        val latLng = LatLng(address.latitude, address.longitude)
                        mMap.addMarker(MarkerOptions().position(latLng).title("Pick Up Location"))
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                // Handle the exception
            }
        }

        // Setup additional map settings if needed
        mMap.uiSettings.isZoomControlsEnabled = true
        // ... other map settings ...
    }
}