package com.example.micamion2

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.properties.Delegates

class TruckDetailed : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private var pickUpLatitude by Delegates.notNull<Double>()
    private var pickUpLongitude by Delegates.notNull<Double>()
    private var dropOffLatitude by Delegates.notNull<Double>()
    private var dropOffLongitude by Delegates.notNull<Double>()
    override fun onCreate(savedInstanceState: Bundle?) {




        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_truck_detailed)
        val backButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        // Set an OnClickListener on the button
        backButton.setOnClickListener {
            // Finish the current activity
            finish()
        }
        val pickUpAddress = intent.getStringExtra("pickUpAddress")
        val dropOffAddress = intent.getStringExtra("dropOffAddress")
        val plates = intent.getStringExtra("plates")
        val type = intent.getStringExtra("type")
        val capacity = intent.getStringExtra("capacity")


        val platesTextView: TextView = findViewById(R.id.platesDetail)
        val capacityTextView: TextView = findViewById(R.id.capacityDetail)
        val typeTextView: TextView = findViewById(R.id.typeDetail)
        val pickUpTextView: TextView = findViewById(R.id.pickUpLocationDetail)
        val dropOffTextView: TextView = findViewById(R.id.dropOffLocationDetail)



        platesTextView.text = "Placas: $plates"
        capacityTextView.text = "Capacidad: $capacity"
        typeTextView.text = when (type) {
            "AN" -> "Tipo: Any"
            "FB" -> "Tipo: Flatbed"
            "DV" -> "Tipo: Dry Van"
            "RF" -> "Tipo: Reefer"
            "LB" -> "Tipo: Lowboy"
            "SD" -> "Tipo: Stepdeck"
            "OT" -> "Tipo: Other"
            else -> "Tipo: Unknown"
        }
        val partsPickUp = pickUpAddress?.split(", ")
        if (partsPickUp != null) {
            if (partsPickUp.size >= 3) {
                val address = partsPickUp.subList(2, partsPickUp.size).joinToString(", ")
                pickUpTextView.text = address
            } else {
                // Handle cases where the string format is not as expected
                pickUpTextView.text = "Address format not recognized"
            }
        }

        pickUpLatitude = partsPickUp?.get(0)?.toDoubleOrNull()!!
        pickUpLongitude = partsPickUp?.get(1)!!.toDoubleOrNull()!!

        val partsDropOff = dropOffAddress?.split(", ")
        if (partsDropOff != null) {
            if (partsDropOff.size >= 3) {
                val address = partsDropOff.subList(2, partsDropOff.size).joinToString(", ")
                dropOffTextView.text = address
            } else {
                // Handle cases where the string format is not as expected
                dropOffTextView.text = "Address format not recognized"
            }
        }

        dropOffLatitude = partsDropOff?.get(0)?.toDoubleOrNull()!!
        dropOffLongitude = partsDropOff?.get(1)!!.toDoubleOrNull()!!



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun setUpMap() {
        // Assuming pickUpLatitude, pickUpLongitude, dropOffLatitude, and dropOffLongitude are available
        val pickUpLatLng = LatLng(pickUpLatitude, pickUpLongitude)
        val dropOffLatLng = LatLng(dropOffLatitude, dropOffLongitude)

        mMap.addMarker(MarkerOptions().position(pickUpLatLng).title("Pick Up Location"))
        mMap.addMarker(MarkerOptions().position(dropOffLatLng).title("Drop Off Location"))

        // Draw a line between pickup and dropoff
        mMap.addPolyline(
            PolylineOptions()
                .add(pickUpLatLng, dropOffLatLng)
                .width(5f)
                .color(Color.RED)
        )

        // Assuming you want to zoom in on the pick-up location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pickUpLatLng, 12f))

        // Enable zoom controls on the map
        mMap.uiSettings.isZoomControlsEnabled = true
        // ... other map settings ...
    }

    // Make sure to call setUpMap() in onMapReady
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setUpMap()
    }

}