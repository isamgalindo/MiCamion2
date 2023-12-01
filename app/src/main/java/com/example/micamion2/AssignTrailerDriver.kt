package com.example.micamion2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AssignTrailerDriver : AppCompatActivity() {

    private val userService = RetrofitInstance.apiUsuario
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assign_trailer_driver)

        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val id = sharedPref.getString("id", "DefaultId") ?: "DefaultIdFallback"
        fetchAndUpdateTrailerData(id)

        val buttonOK = findViewById<Button>(R.id.AssignButton)
        buttonOK.setOnClickListener {
            if (id != null) {
                val request = AssignDriverRequest("5")
                userService.assignTrailer(request).enqueue(object : Callback<Void>  {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        // The request was successful (status code 200-299)
                        Toast.makeText(applicationContext, "Bien", Toast.LENGTH_SHORT).show()
                        fetchAndUpdateTrailerData(id)
                        // Perform actions based on successful response
                    } else {
                        val errorBody = response.errorBody()?.string()
                        // The server responded with a status code that isn't in the range 200-299
                        Toast.makeText(applicationContext, "Error: $errorBody", Toast.LENGTH_SHORT).show()
                        // Handle the server response error
                    }
                    // Handle response
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(applicationContext, "Mal", Toast.LENGTH_SHORT).show()
                }
            })
            }else{
                Toast.makeText(applicationContext, "User ID is null", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchAndUpdateTrailerData(userId: String) {
        Thread {
            try {
                val response = userService.getTrailerByDriver(userId).execute()
                // Check if the response is successful and has body
                if (response.isSuccessful && response.body() != null) {
                    val trailers = response.body()!!

                    // Check if the trailers list is not empty
                    if (trailers.isNotEmpty()) {
                        // Assuming you want to display the first trailer's properties
                        val trailer = trailers.first()

                        runOnUiThread {
                            // Update the TextView on the main thread
                            findViewById<TextView>(R.id.platesTrailer).text = trailer.plates
                            findViewById<TextView>(R.id.capacityTrailer).text =
                                trailer.capacity.toString()
                            findViewById<TextView>(R.id.statusTrailer).text = trailer.status

                        }
                    } else {
                        runOnUiThread {
                            findViewById<TextView>(R.id.platesTrailer).text =
                                "No trailer assign"
                        }
                    }
                } else {
                    runOnUiThread {
                        findViewById<TextView>(R.id.platesTrailer).text =
                            "Response not successful"
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    // Update UI for error on the main thread
                    findViewById<TextView>(R.id.platesTrailer).text = "Request failed"
                }
            }
        }.start()
    }
}

