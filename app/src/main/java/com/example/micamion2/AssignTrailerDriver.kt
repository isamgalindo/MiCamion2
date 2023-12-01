package com.example.micamion2

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AssignTrailerDriver : AppCompatActivity() {

    private val userService = RetrofitInstance.apiUsuario
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assign_trailer_driver)


        Thread {
            try {
                val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
                val id = sharedPref.getString("id", "DefaultId") ?: "DefaultIdFallback"
                val response = userService.getTrailerByDriver(id).execute()
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
                            findViewById<TextView>(R.id.capacityTrailer).text = trailer.capacity.toString()
                            findViewById<TextView>(R.id.statusTrailer).text = trailer.status

                        }
                    } else {
                        runOnUiThread {
                            findViewById<TextView>(R.id.platesTrailer).text = "No trailer assign"
                        }
                    }
                } else {
                    runOnUiThread {
                        findViewById<TextView>(R.id.platesTrailer).text = "Response not successful"
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

