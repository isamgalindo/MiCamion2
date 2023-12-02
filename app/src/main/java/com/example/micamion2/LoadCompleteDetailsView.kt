package com.example.micamion2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoadCompleteDetailsView : AppCompatActivity() {

    var createdLoadId =0
    var createdOriginId=0
    var createdDestinationId=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_complete_details_view)
        val sharedPref = getSharedPreferences("LoadDetails", Context.MODE_PRIVATE)

        val editor = sharedPref.edit()

        val userId = sharedPref.getInt("userId", 0)





        val type = sharedPref.getString("loadType", "") ?: ""
        val weight = sharedPref.getString("loadWeight", "") ?: ""
        val volume = sharedPref.getString("loadVolume", "") ?: ""
        val pickUpAddress = sharedPref.getString("originAddress", "") ?: ""
        val pickUpCity = sharedPref.getString("originCity", "") ?: ""
        val pickUpCountry = sharedPref.getString("originCountry", "") ?: ""

        val dropOffAddress = sharedPref.getString("destinationAddress", "") ?: ""
        val dropOffCity = sharedPref.getString("destinationCity", "") ?: ""
        val dropOffCountry = sharedPref.getString("destinationCountry", "") ?: ""

        val recipientMail = sharedPref.getString("recipientMail", "") ?: ""
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

        loadTypeTextView.text = type
        loadWeightTextView.text = weight
        loadVolumeTextView.text = volume
        loadpickUpDateTextView.text = pickUpDate
        loadDropOffDateTextView.text = dropOffDate

        val buttonOK = findViewById<Button>(R.id.okButton)
        buttonOK.setOnClickListener {

            getLoad(userId, type, weight.toInt(), volume.toInt(), pickUpAddress, pickUpCity, pickUpCountry, dropOffAddress, dropOffCity, dropOffCountry, recipientMail, pickUpDate, dropOffDate)


            val intent = Intent(this, SendView::class.java)
            startActivity(intent)
        }


    }

    private fun getLoad(userId: Int,
                        type: String,
                        weight: Int,
                        volume: Int,
                        pickUpAddress: String,
                        pickUpCity: String,
                        pickUpCountry: String,
                        dropOffAddress: String,
                        dropOffCity: String,
                        dropOffCountry: String,
                        recipientMail: String,
                        pickUpDate: String,
                        dropOffDate: String){


        val load = Load()
        load.type = type
        load.weight = weight.toInt()
        load.volume = volume.toInt()
        load.trailerType = "FB"

        //Crear Load
        val loadCall = RetrofitInstance.apiUsuario.createLoad(load)
        loadCall.enqueue(object : Callback<Load> {
            override fun onResponse(call: Call<Load>, response: Response<Load>) {
                if (response.isSuccessful) {
                    createdLoadId = response.body()?.id!!

                    getOrigin(pickUpDate, dropOffDate, pickUpAddress, pickUpCity, pickUpCountry, dropOffAddress, dropOffCity, dropOffCountry, userId)

                    Toast.makeText(this@LoadCompleteDetailsView, "Load created successfully", Toast.LENGTH_SHORT).show()

                } else {
                    val errorBody = response.errorBody()?.string()
                    println("$errorBody")
                    Toast.makeText(this@LoadCompleteDetailsView, "Error creating load, $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Load>, t: Throwable) {
                Toast.makeText(this@LoadCompleteDetailsView, "Error conecting to the server", Toast.LENGTH_SHORT).show()
            }
        })

    }


    private fun getOrigin(pickUpDate: String, dropOffDate: String,pickUpAddress: String, pickUpCity: String, pickUpCountry: String, dropOffAddress: String, dropOffCity: String, dropOffCountry: String,userId: Int) {


        val origin = AccessPoint()
        origin.country = pickUpCountry
        origin.city = pickUpCity
        origin.address = pickUpAddress
        origin.before = pickUpDate
        origin.after = dropOffDate

        //Crear Load
        val originCall = RetrofitInstance.apiUsuario.createAccessPoint(origin)
        originCall.enqueue(object : Callback<AccessPoint> {
            override fun onResponse(call: Call<AccessPoint>, response: Response<AccessPoint>) {
                if (response.isSuccessful) {
                    createdOriginId = response.body()?.id!!
                    getDestination(pickUpDate,dropOffDate,dropOffAddress,dropOffCity,dropOffCountry,userId)
                    Toast.makeText(
                        this@LoadCompleteDetailsView,
                        "Origin created successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Error Access point", "$errorBody")
                    Toast.makeText(
                        this@LoadCompleteDetailsView,
                        "Error creating Origin, $errorBody",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<AccessPoint>, t: Throwable) {
                Toast.makeText(
                    this@LoadCompleteDetailsView,
                    "Error conecting to the server",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    private fun getDestination(pickUpDate: String, dropOffDate: String,dropOffAddress: String, dropOffCity: String, dropOffCountry: String,userId: Int){


        val destination = AccessPoint()
        destination.country = dropOffCountry
        destination.city = dropOffCity
        destination.address = dropOffAddress
        destination.before = pickUpDate
        destination.after = dropOffDate

        //Crear Load
        val destinationCall = RetrofitInstance.apiUsuario.createAccessPoint(destination)
        destinationCall.enqueue(object : Callback<AccessPoint> {
            override fun onResponse(call: Call<AccessPoint>, response: Response<AccessPoint>) {
                if (response.isSuccessful) {
                    createdDestinationId = response.body()?.id!!

                    getTrip(userId)

                    Toast.makeText(this@LoadCompleteDetailsView, "destination created successfully", Toast.LENGTH_SHORT).show()

                } else {
                    val errorBody = response.errorBody()?.string()
                    println("$errorBody")
                    Toast.makeText(this@LoadCompleteDetailsView, "Error creating destination, $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AccessPoint>, t: Throwable) {
                Toast.makeText(this@LoadCompleteDetailsView, "Error conecting to the server", Toast.LENGTH_SHORT).show()
            }
        })
    }





    private fun getTrip(userId:Int){

        val trip = Trip()
        trip.loadOwner = userId
        trip.load = createdLoadId
        trip.pickup = createdOriginId
        trip.dropoff = createdDestinationId
        trip.status = "TA"
        //Crear Load
        val tripCall = RetrofitInstance.apiUsuario.createTrip(trip)
        tripCall.enqueue(object : Callback<Trip> {
            override fun onResponse(call: Call<Trip>, response: Response<Trip>) {
                if (response.isSuccessful) {
                    val createdTripId = response.body()?.id
                    Toast.makeText(
                        this@LoadCompleteDetailsView,
                        "Trip created successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    val errorBody = response.errorBody()?.string()
                    println("$errorBody")
                    Log.e("error","$errorBody")
                    Toast.makeText(
                        this@LoadCompleteDetailsView,
                        "Error creating Trip, $errorBody",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onFailure(call: Call<Trip>, t: Throwable) {
                Toast.makeText(this@LoadCompleteDetailsView, "Error conecting to the server", Toast.LENGTH_SHORT).show()
            }
        })


    }


}