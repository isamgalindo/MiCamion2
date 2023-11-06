package com.example.micamion2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class TrucksView : AppCompatActivity() {
    private lateinit var searchView: SearchView
    private lateinit var container: LinearLayout
    private lateinit var allCardViews: List<CardView>
    private val userService = RetrofitInstance.apiUsuario
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trucks_view)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.truckMenu
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.truckMenu -> {
                    true
                }
                R.id.home ->{
                    val intent = Intent(this@TrucksView, ServicesTruckOwner::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.profile ->{
                    val intent = Intent(this@TrucksView, ProfileTruckOwner::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                // Add other menu items here...
                else -> false
            }
        }

        val button = findViewById<Button>(R.id.createTruckButton)
        button.setOnClickListener {
            val intent = Intent(this, CreateTruckDetails::class.java)
            startActivity(intent)
        }
        val searchView = findViewById<SearchView>(R.id.searchView)

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
        userService.getAllTrailers().enqueue(object : Callback<List<Trailer>> {
            override fun onResponse(call: Call<List<Trailer>>, response: Response<List<Trailer>>) {
                if (response.isSuccessful) {
                    val trailers = response.body()
                    if (trailers != null) {
                        // Get reference to the LinearLayout container
                        val container = findViewById<LinearLayout>(R.id.linearLayout)
                        trailers.forEach { trailer ->
                            val inflater = LayoutInflater.from(this@TrucksView)
                            val tripCard = inflater.inflate(R.layout.trip_card, container, false).apply {
                                findViewById<TextView>(R.id.weight).text = trailer.capacity.toString()
                                findViewById<TextView>(R.id.pickUpAddress).text = trailer.pickup
                                findViewById<TextView>(R.id.dropOffAddress).text = trailer.dropoff
                                if (trailer.type == "AN") {
                                    findViewById<TextView>(R.id.Name).text = "Any"
                                }
                                if (trailer.type == "FB") {
                                    findViewById<TextView>(R.id.Name).text = "Flatbed"
                                }
                                if (trailer.type == "DV") {
                                    findViewById<TextView>(R.id.Name).text = "Dry Van"
                                }
                                if (trailer.type == "RF") {
                                    findViewById<TextView>(R.id.Name).text = "Reefer"
                                }
                                if (trailer.type == "LB") {
                                    findViewById<TextView>(R.id.Name).text = "Lowboy"
                                }
                                if (trailer.type == "SD") {
                                    findViewById<TextView>(R.id.Name).text = "Stepdeck"
                                }
                                if (trailer.type == "OT") {
                                    findViewById<TextView>(R.id.Name).text = "Stepdeck"
                                }
                                if (trailer.status == "AV") {
                                    findViewById<TextView>(R.id.status).text = "Available"
                                }
                                if (trailer.status == "IT") {
                                    findViewById<TextView>(R.id.status).text = "In transit"
                                }
                                if (trailer.status == "UN") {
                                    findViewById<TextView>(R.id.status).text = "Unavailable"
                                }
                                container.addView(this)
                            }
                        }
                    } else {
                        Toast.makeText(applicationContext, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<List<Trailer>>, t: Throwable) {
                Toast.makeText(applicationContext, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
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