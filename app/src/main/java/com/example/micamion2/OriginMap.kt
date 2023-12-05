package com.example.micamion2

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.micamion2.databinding.ActivityFuncAfBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.Marker

class OriginMap : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnCameraMoveListener {


    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityFuncAfBinding
    private lateinit var lastLocation: LatLng
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var saveLocationButton: Button
    private lateinit var geocoder: Geocoder
    private var previousMarker: Marker? = null


    companion object {
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFuncAfBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // Find the button in the layout
        saveLocationButton = findViewById(R.id.confirmLocationButton)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this)

        // Set a click listener for the button
        saveLocationButton.setOnClickListener {
            saveLocationInfo()
            val sharedPreferences = getSharedPreferences("LoadDetails", Context.MODE_PRIVATE)
            val userType = sharedPreferences.getString("userType","")

            if (userType=="LO") {
                val intent = Intent(this, CreateLoadDestinationView::class.java)

                startActivity(intent)
                finish()
            }
            else {
                val intent = Intent(this, CreateTruckLocation::class.java)

                startActivity(intent)
                finish()
            }

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnCameraMoveListener(this)

        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                lastLocation = LatLng(location.latitude, location.longitude)

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, 12f))

            }
        }
    }

    private fun placeMarkerOnMap(latlng: LatLng): Marker? {
        val markerOptions = MarkerOptions().position(latlng)
        return mMap.addMarker(markerOptions)
    }

    override fun onCameraMove() {
        val center = mMap.cameraPosition.target

        // Remove the previous marker if it exists
        previousMarker?.remove()

        // Place a new marker at the center
        previousMarker = placeMarkerOnMap(center)
    }

    private fun saveLocationInfo() {
        val center = mMap.cameraPosition.target
        val addresses: MutableList<Address>? = geocoder.getFromLocation(center.latitude, center.longitude, 1)
        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                val address: String = addresses[0].getAddressLine(0) ?: ""
                val city: String = addresses[0].locality ?: ""
                val country: String = addresses[0].countryName ?: ""

                // Save address, city, and country in SharedPreferences
                val sharedPreferences = getSharedPreferences("LoadDetails", Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putString("originAddress", address)
                    putString("originCity", city)
                    putString("originCountry", country)
                    apply()
                }
                    val sharedTruckPreferences = getSharedPreferences("TruckDetails", Context.MODE_PRIVATE)
                    with(sharedTruckPreferences.edit()) {
                        putString("originAddress", address)

                        apply()
                    }
                }


        }
    }
}