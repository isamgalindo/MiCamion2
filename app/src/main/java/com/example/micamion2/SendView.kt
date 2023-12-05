package com.example.micamion2

import android.content.Intent
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
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class SendView : AppCompatActivity() {
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
        setContentView(R.layout.activity_send_view)
        container = findViewById(R.id.linearLayout)
        val searchView: SearchView = findViewById(R.id.searchView)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.loadmenu
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.loadmenu -> {
                    true
                }
                R.id.home ->{
                    val intent = Intent(this@SendView, ServicesCompanyPersona::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.profile ->{
                    val intent = Intent(this@SendView, ProfileView::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                // Add other menu items here...
                else -> false
            }
        }



        val button = findViewById<Button>(R.id.createLoadButton)
        button.setOnClickListener {
            val intent = Intent(this, CreateLoadDetailsView::class.java)
            startActivity(intent)
        }

        // Initialize the list of CardViews by searching through the entire view hierarchy
        allCardViews = findAllCardViews(window.decorView)

        // Set up a listener for the SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterCardViews(newText?.toLowerCase(Locale.getDefault()) ?: "")
                return true
            }
        })

        loadTrips()

    }
    private fun loadTrips() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val trips = withContext(Dispatchers.IO) {
                    userService.getAllTrips().execute().body() ?: listOf()
                }

                trips.forEach { trip ->
                    val tripCard = LayoutInflater.from(this@SendView)
                        .inflate(R.layout.trip_card, container, false)
                    setupTripCard(tripCard, trip)
                    container.addView(tripCard)
                }
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "No internet connection", Toast.LENGTH_SHORT).show()
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
            tripCard.findViewById<TextView>(R.id.status).setBackgroundColor(when (trip.status) {
                "DE" -> ContextCompat.getColor(this@SendView, R.color.green) // Replace with your color resource
                "IP" -> ContextCompat.getColor(this@SendView, R.color.Deepblue) // Replace with your color resource
                "TA" -> ContextCompat.getColor(this@SendView, R.color.yellow) // Replace with your color resource
                "CA" -> ContextCompat.getColor(this@SendView, R.color.red) // Replace with your color resource
                else -> ContextCompat.getColor(this@SendView, R.color.Gray) // Replace with your color resource
            })

            // Fetch Load details
            val load = withContext(Dispatchers.IO) {
                try {
                    userService.getLoadById(trip.load).execute().body()
                } catch (e: Exception) {
                    Log.e("SendView", "Failed to fetch Load details", e)
                    null
                }
            }
            load?.let {
                tripCard.findViewById<TextView>(R.id.Name).text = it.type
                tripCard.findViewById<TextView>(R.id.weight).text = it.weight.toString()
            } ?: run {
                // Show a toast or update UI for no internet connection
                Toast.makeText(this@SendView, "No internet connection", Toast.LENGTH_SHORT).show()
            }
            val pickUpAP = async(Dispatchers.IO) { getAccessPoint(trip.pickup) }
            val dropOffAP = async(Dispatchers.IO) { getAccessPoint(trip.dropoff) }

            val pickUpAddress = pickUpAP.await()
            val dropOffAddress = dropOffAP.await()

            tripCard.findViewById<TextView>(R.id.pickUpAddress).text =
                "${pickUpAddress.address}, ${pickUpAddress.city}, ${pickUpAddress.country}"
            tripCard.findViewById<TextView>(R.id.dropOffAddress).text =
                "${dropOffAddress.address}, ${dropOffAddress.city}, ${dropOffAddress.country}"

            val beforeDateText = try {
                val zonedDateTime = ZonedDateTime.parse(pickUpAddress.before)
                val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
                zonedDateTime.format(outputFormatter)
            } catch (e: Exception) {
                pickUpAddress.before // Return the original string if parsing fails
            }
            val afterDateText = try {
                val zonedDateTime = ZonedDateTime.parse(dropOffAddress.before)
                val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
                zonedDateTime.format(outputFormatter)
            } catch (e: Exception) {
                dropOffAddress.before // Return the original string if parsing fails
            }

            tripCard.findViewById<TextView>(R.id.pickUpDate).text = beforeDateText
            tripCard.findViewById<TextView>(R.id.dropOffDate).text = afterDateText

            tripCard.setOnClickListener {
                Intent(this@SendView, HistoryTripsDetailed::class.java).apply {
                    putExtra("pickUpAddress", pickUpAddress.address)
                    putExtra("pickUpCity", pickUpAddress.city)
                    putExtra("pickUpCountry", pickUpAddress.country)
                    putExtra("dropOffAddress", dropOffAddress.address)
                    putExtra("dropOffCity", dropOffAddress.city)
                    putExtra("dropOffCountry", dropOffAddress.country)
                    putExtra("pickUpDate", beforeDateText)
                    putExtra("dropOffDate", afterDateText)
                    load?.let {
                        putExtra("loadType", it.type)
                        putExtra("loadWeight", it.weight.toString())
                    }
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
            val childTextViews = findAllTextViews(cardView)

            // Check if any of the child TextViews contain the search query
            val cardVisible = childTextViews.any { it.text.toString().toLowerCase(Locale.getDefault()).contains(query) }

            cardView.visibility = if (cardVisible) View.VISIBLE else View.GONE
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