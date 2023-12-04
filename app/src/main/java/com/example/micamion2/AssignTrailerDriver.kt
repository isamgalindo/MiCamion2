package com.example.micamion2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
                        findViewById<TextView>(R.id.platesTrailer).text = "${response.code()}"
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    findViewById<TextView>(R.id.platesTrailer).text = "Request failed"
                }
            }
        }.start()
    }
}


