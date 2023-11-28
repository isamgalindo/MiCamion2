package com.example.micamion2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class HistoryView : AppCompatActivity() {
    private lateinit var searchView: SearchView
    private lateinit var container: LinearLayout
    private lateinit var allCardViews: List<CardView>
    private var userId =""
    private var name =""
    private var email =""
    private var userType =""
    private var lastName =""
    private var phone =""
    private val userService = RetrofitInstance.apiUsuario
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_view)
        container = findViewById(R.id.linearLayout)
        val searchView: SearchView = findViewById(R.id.searchView)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.history
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.history -> true
                R.id.home -> {
                    startActivity(Intent(this@HistoryView, ServicesDriver::class.java))
                    finish()
                    true
                }
                R.id.profile -> {
                    startActivity(Intent(this@HistoryView, ProfileDriver::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        loadTrips()
        setupSearchView(searchView)
    }

    private fun loadTrips() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val trips = withContext(Dispatchers.IO) {
                    userService.getAllTrips().execute().body() ?: listOf()
                }

                trips.forEach { trip ->
                    val tripCard = LayoutInflater.from(this@HistoryView)
                        .inflate(R.layout.trip_card, container, false)
                    setupTripCard(tripCard, trip)
                    container.addView(tripCard)
                }
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("HistoryView", "Failed to load trips", e)
            }
        }
    }

    private fun setupTripCard(tripCard: View, trip: Trip) {
        CoroutineScope(Dispatchers.Main).launch {
            tripCard.findViewById<TextView>(R.id.status).text = when (trip.status) {
                "DE" -> "Delivered"
                "IP" -> "In Progress"
                "TA" -> "To Assign"
                "CA" -> "Cancelled"
                else -> "Unknown Status"
            }

            // Fetch Load details
            val load = withContext(Dispatchers.IO) {
                try {
                    userService.getLoadById(trip.load).execute().body()
                } catch (e: Exception) {
                    null
                }
            }
            load?.let {
                tripCard.findViewById<TextView>(R.id.Name).text = it.type
                tripCard.findViewById<TextView>(R.id.weight).text = it.weight.toString()
            }
            val pickUpAP = async(Dispatchers.IO) { getAccessPoint(trip.pickup) }
            val dropOffAP = async(Dispatchers.IO) { getAccessPoint(trip.dropoff) }

            val pickUpAddress = pickUpAP.await()
            val dropOffAddress = dropOffAP.await()

            tripCard.findViewById<TextView>(R.id.pickUpAddress).text =
                "${pickUpAddress.address}, ${pickUpAddress.city}, ${pickUpAddress.country}"
            tripCard.findViewById<TextView>(R.id.dropOffAddress).text =
                "${dropOffAddress.address}, ${dropOffAddress.city}, ${dropOffAddress.country}"

            tripCard.setOnClickListener {
                Intent(this@HistoryView, HistoryTripsDetailed::class.java).apply {
                    putExtra("pickUpAddress", pickUpAddress.address)
                    putExtra("pickUpCity", pickUpAddress.city)
                    putExtra("pickUpCountry", pickUpAddress.country)
                    putExtra("dropOffAddress", dropOffAddress.address)
                    putExtra("dropOffCity", dropOffAddress.city)
                    putExtra("dropOffCountry", dropOffAddress.country)
                    startActivity(this)
                }
            }
        }
    }

    private suspend fun getAccessPoint(id: Int): AccessPoint {
        return withContext(Dispatchers.IO) {
            userService.getAccessPointById(id).execute().body()
                ?: throw Exception("Access point not found")
        }
    }

    private fun setupSearchView(searchView: SearchView) {
        allCardViews = findAllCardViews(container)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterCardViews(newText.orEmpty().toLowerCase(Locale.getDefault()))
                return true
            }
        })
    }

    private fun findAllCardViews(view: View): List<CardView> {
        val cardViews = mutableListOf<CardView>()
        if (view is CardView) {
            cardViews.add(view)
        } else if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                cardViews.addAll(findAllCardViews(view.getChildAt(i)))
            }
        }
        return cardViews
    }

    private fun filterCardViews(query: String) {
        for (cardView in allCardViews) {
            val isVisible = findAllTextViews(cardView).any {
                it.text.toString().toLowerCase(Locale.getDefault()).contains(query)
            }
            cardView.visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }

    private fun findAllTextViews(view: View): List<TextView> {
        val textViews = mutableListOf<TextView>()
        if (view is TextView) {
            textViews.add(view)
        } else if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                textViews.addAll(findAllTextViews(view.getChildAt(i)))
            }
        }
        return textViews
    }
}