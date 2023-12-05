package com.example.micamion2

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AssignTrailerDriver : AppCompatActivity() {

    private val userService = RetrofitInstance.apiUsuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assign_trailer_driver)

        val sharedPref = getSharedPreferences("LoadDetails", Context.MODE_PRIVATE)
        val id = sharedPref.getInt("userId", 0 ).toString() ?: "DefaultIdFallback"

        // Fetch and update trailer data initially
        fetchAndUpdateTrailerData(id)

        val backButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        // Set an OnClickListener on the button
        backButton.setOnClickListener {
            // Finish the current activity
            finish()
        }
        val buttonOK = findViewById<Button>(R.id.AssignButton)
        buttonOK.setOnClickListener {
            val request = AssignDriverRequest(id)
            userService.assignTrailer(request).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Bien", Toast.LENGTH_SHORT).show()
                        // Fetch and update trailer data after successful assignment
                        fetchAndUpdateTrailerData(id)
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(applicationContext, "Error: $errorBody", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(applicationContext, "Mal", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun fetchAndUpdateTrailerData(userId: String) {
        Thread {
            try {
                val response = userService.getTrailerByDriver(userId).execute()
                if (response.isSuccessful && response.body() != null) {
                    val trailers = response.body()!!

                    if (trailers.isNotEmpty()) {
                        val trailer = trailers.first()
                        runOnUiThread {
                            findViewById<TextView>(R.id.platesTrailer).text = trailer.plates
                            val partsPickUp = trailer.pickup.split(", ")
                            if (partsPickUp.size >= 3) {
                                val address = partsPickUp.subList(2, partsPickUp.size).joinToString(", ")
                                findViewById<TextView>(R.id.pickUpAddress).text = address
                            } else {
                                // Handle cases where the string format is not as expected
                                findViewById<TextView>(R.id.pickUpAddress).text = "Address format not recognized"
                            }
                            val partsDropOff = trailer.dropoff.split(", ")
                            if (partsDropOff.size >= 3) {
                                val address = partsDropOff.subList(2, partsDropOff.size).joinToString(", ")
                                findViewById<TextView>(R.id.dropOffAddress).text = address
                            } else {
                                // Handle cases where the string format is not as expected
                                findViewById<TextView>(R.id.dropOffAddress).text = "Address format not recognized"
                            }
                            if (trailer.status == "AV") {
                                findViewById<TextView>(R.id.statusTrailer).text = "Available"
                            }
                            if (trailer.status == "IT") {
                                findViewById<TextView>(R.id.statusTrailer).text = "In transit"
                            }
                            if (trailer.status == "UN") {
                                findViewById<TextView>(R.id.statusTrailer).text = "Unavailable"
                            }
                            findViewById<TextView>(R.id.statusTrailer).setBackgroundColor(when (trailer.status) {
                                "AV" -> ContextCompat.getColor(this@AssignTrailerDriver, R.color.green) // Replace with your color resource
                                "IT" -> ContextCompat.getColor(this@AssignTrailerDriver, R.color.yellow) // Replace with your color resource
                                "UN" -> ContextCompat.getColor(this@AssignTrailerDriver, R.color.red) // Replace with your color resource
                                else -> ContextCompat.getColor(this@AssignTrailerDriver, R.color.Gray) // Replace with your color resource
                            })
                            findViewById<TextView>(R.id.platesTrailer).text = trailer.plates
                            if (trailer.type == "AN") {
                                findViewById<TextView>(R.id.capacityTrailer).text = "${trailer.capacity} - Any"
                            }
                            if (trailer.type == "FB") {
                                findViewById<TextView>(R.id.capacityTrailer).text = "${trailer.capacity} - Flatbed"
                            }
                            if (trailer.type == "DV") {
                                findViewById<TextView>(R.id.capacityTrailer).text = "${trailer.capacity} - Dry Van"
                            }
                            if (trailer.type == "RF") {
                                findViewById<TextView>(R.id.capacityTrailer).text = "${trailer.capacity} - Reefer"
                            }
                            if (trailer.type == "LB") {
                                findViewById<TextView>(R.id.capacityTrailer).text = "${trailer.capacity} - Lowboy"
                            }
                            if (trailer.type == "SD") {
                                findViewById<TextView>(R.id.capacityTrailer).text = "${trailer.capacity} - Stepdeck"
                            }
                            if (trailer.type == "OT") {
                                findViewById<TextView>(R.id.capacityTrailer).text = "${trailer.capacity} - Other"
                            }
                        }
                    } else {
                        runOnUiThread {
                            findViewById<TextView>(R.id.platesTrailer).text = "No trailer assign"
                        }
                    }
                } else {
                    runOnUiThread {
                        findViewById<TextView>(R.id.platesTrailer).text = "${response.code()}"
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    findViewById<TextView>(R.id.platesTrailer).text = "No internet connection"
                }
            }
        }.start()
    }
}


