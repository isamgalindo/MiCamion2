package com.example.micamion2

import android.graphics.Color
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse
import java.io.IOException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class CurrentTripDriver : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private val userService = RetrofitInstance.apiUsuario
    private var pickUpLocation: String = ""
    private var dropOffLocation: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_trip_driver)

        val backButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        // Set an OnClickListener on the button
        backButton.setOnClickListener {
            // Finish the current activity
            finish()
        }

        val loadCache = mutableMapOf<String, Load>()
        val accessPointCache = mutableMapOf<String, AccessPoint>()

        GlobalScope.launch(Dispatchers.Main) {
            val tripsResponse = userService.getAllTrips().awaitResponse()
            if (tripsResponse.isSuccessful) {
                val trips = tripsResponse.body()
                trips?.firstOrNull { it.status == "IP" }?.let { trip ->
                    val load = loadCache.getOrPut(trip.load.toString()) {
                        userService.getLoadById(trip.load).awaitResponse().body()!!
                    }

                    findViewById<TextView>(R.id.loadTypeDetail).text = load.type
                    findViewById<TextView>(R.id.weightDetail).text = load.weight.toString()

                    val pickUpAccessPoint = accessPointCache.getOrPut(trip.pickup.toString()) {
                        userService.getAccessPointById(trip.pickup).awaitResponse().body()!!
                    }
                    val beforeDateText = try {
                        val zonedDateTime = ZonedDateTime.parse(pickUpAccessPoint.before)
                        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
                        zonedDateTime.format(outputFormatter)
                    } catch (e: Exception) {
                        pickUpAccessPoint.before // Return the original string if parsing fails
                    }

                    findViewById<TextView>(R.id.pickUpLocationDetail).text = "${pickUpAccessPoint.address}, ${pickUpAccessPoint.city}, ${pickUpAccessPoint.country}"
                    findViewById<TextView>(R.id.pickUpDateDetail).text = beforeDateText

                    val dropOffAccessPoint = accessPointCache.getOrPut(trip.dropoff.toString()) {
                        userService.getAccessPointById(trip.dropoff).awaitResponse().body()!!
                    }

                    val afterDateText = try {
                        val zonedDateTime = ZonedDateTime.parse(dropOffAccessPoint.before)
                        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
                        zonedDateTime.format(outputFormatter)
                    } catch (e: Exception) {
                        dropOffAccessPoint.before // Return the original string if parsing fails
                    }

                    findViewById<TextView>(R.id.dropOffLocationDetail).text = "${dropOffAccessPoint.address}, ${dropOffAccessPoint.city}, ${dropOffAccessPoint.country}"

                    findViewById<TextView>(R.id.dropOffDateDetail).text = afterDateText

                    pickUpLocation = "${pickUpAccessPoint.address}, ${pickUpAccessPoint.city}, ${pickUpAccessPoint.country}"
                    dropOffLocation = "${dropOffAccessPoint.address}, ${dropOffAccessPoint.city}, ${dropOffAccessPoint.country}"
                    setUpMap()

                } ?: run {
                    Toast.makeText(applicationContext, "No trip in progress available", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Error: ${tripsResponse.code()}", Toast.LENGTH_SHORT).show()
            }
        }.apply {
            invokeOnCompletion { throwable ->
                throwable?.let {
                    Toast.makeText(applicationContext, "Failure: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
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
            // Handle the exception
        }

        mMap.uiSettings.isZoomControlsEnabled = true
        // ... other map settings ...
    }


}