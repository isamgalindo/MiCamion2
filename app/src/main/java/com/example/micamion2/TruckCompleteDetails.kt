package com.example.micamion2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TruckCompleteDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_truck_complete_details)

        val sharedOwnerPref = getSharedPreferences("LoadDetails", Context.MODE_PRIVATE)

        val owner = sharedOwnerPref.getInt("userId", 0)


        val sharedPref = getSharedPreferences("TruckDetails", Context.MODE_PRIVATE)

        val model = sharedPref.getString("modelTruck", "") ?: ""
        val brand = sharedPref.getString("brandTruck", "") ?: ""
        val plates = sharedPref.getString("platesTruck", "") ?: ""
        val weight = sharedPref.getString("weightTruck", "") ?: ""
        val volume = sharedPref.getString("volumeTruck", "") ?: ""
        val driver = sharedPref.getInt("driverTruck", 0)
        val pickUp = sharedPref.getString("pickUpLocation", "") ?: ""
        val dropOff = sharedPref.getString("dropOffLocation", "") ?: ""


        // Get the TextView reference
        val modelTextView: TextView = findViewById(R.id.modelReview)
        val driverTextView: TextView = findViewById(R.id.driverReview)
        val weightTextView: TextView = findViewById(R.id.weightReview)
        val volumeTextView: TextView = findViewById(R.id.volumeReview)
        val pickUpTextView: TextView = findViewById(R.id.truckPickUpReview)
        val dropOffTextView: TextView = findViewById(R.id.truckDropOffReview)
        // Set the text using the string resource with placeholder

        modelTextView.text = "$brand $model $plates"
        driverTextView.text = driver.toString()
        weightTextView.text = weight
        volumeTextView.text = volume
        pickUpTextView.text = pickUp
        dropOffTextView.text = dropOff


        val buttonOK = findViewById<Button>(R.id.okButton)
        buttonOK.setOnClickListener {
            getTruck(owner,weight.toInt(),volume.toInt(),pickUp,dropOff,driver,plates)
            val intent = Intent(this, TrucksView::class.java)
            startActivity(intent)
        }
    }

    private fun getTruck(userId:Int, weight: Int, volume: Int, pickUp: String, dropOff: String , driver:Int, plates:String){

        val truck = Trailer()
        truck.owner = userId
        truck.driver = driver
        truck.capacity = weight
        truck.volume = volume
        truck.pickup = pickUp
        truck.dropoff=dropOff
        truck.plates = plates
        //Crear Load
        val truckCall = RetrofitInstance.apiUsuario.createTrailer(truck)
        truckCall.enqueue(object : Callback<Trailer> {
            override fun onResponse(call: Call<Trailer>, response: Response<Trailer>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@TruckCompleteDetails,
                        "Trailer created successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    val errorBody = response.errorBody()?.string()
                    println("$errorBody")
                    Log.e("error","$errorBody")
                    Toast.makeText(
                        this@TruckCompleteDetails,
                        "Error creating Trailer, $errorBody",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("Error Body", errorBody.toString())
                }
            }
            override fun onFailure(call: Call<Trailer>, t: Throwable) {
                Toast.makeText(this@TruckCompleteDetails, "Error conecting to the server", Toast.LENGTH_SHORT).show()
            }
        })


    }
}