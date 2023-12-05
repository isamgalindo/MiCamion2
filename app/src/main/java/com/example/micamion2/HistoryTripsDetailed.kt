package com.example.micamion2

import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.IOException

class HistoryTripsDetailed : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var pickUpLocation: String
    private lateinit var dropOffLocation: String
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
        dropOffLocation = "${dropOffAddress}, ${dropOffCity}, $dropOffCountry"


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setUpMap()
    }

    private fun setUpMap() {
        val geocoder = Geocoder(this)
        try {
            val pickUpAddressList = geocoder.getFromLocationName(pickUpLocation, 1)
            val dropOffAddressList = geocoder.getFromLocationName(dropOffLocation, 1)

            if (pickUpAddressList != null) {
                if (dropOffAddressList != null) {
                    if (pickUpAddressList.isNotEmpty() && dropOffAddressList.isNotEmpty()) {
                        val pickUpAddress = pickUpAddressList[0]
                        val dropOffAddress = dropOffAddressList[0]

                        val pickUpLatLng = LatLng(pickUpAddress.latitude, pickUpAddress.longitude)
                        val dropOffLatLng = LatLng(dropOffAddress.latitude, dropOffAddress.longitude)

                        mMap.addMarker(MarkerOptions().position(dropOffLatLng).title("Drop Off Location"))

                        // Draw a line between pickup and dropoff
                        mMap.addPolyline(
                            PolylineOptions()
                                .add(pickUpLatLng, dropOffLatLng)
                                .width(5f)
                                .color(Color.RED)
                        )

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pickUpLatLng, 12f))
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this@HistoryTripsDetailed, "No internet connection", Toast.LENGTH_SHORT).show()
            // Handle the exception
        }

        mMap.uiSettings.isZoomControlsEnabled = true
        // ... other map settings ...
    }
}