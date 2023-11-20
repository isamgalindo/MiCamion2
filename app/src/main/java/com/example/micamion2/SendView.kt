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
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

        getUserID()





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


    private fun getUserID() {
        userService.getUserIdByEmail(email).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val jsonResponse = response.body()
                    if (jsonResponse != null) {
                        val id = jsonResponse.get("id").toString()
                        getTrips(id)
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun getTrips(userId: String) {
        userService.getTripsByLoadOwner(userId).enqueue(object : Callback<List<Trip>> {
            override fun onResponse(call: Call<List<Trip>>, response: Response<List<Trip>>) {
                if (response.isSuccessful) {
                    val trips = response.body()
                    if (trips != null) {
                        // Get reference to the LinearLayout container
                        val container = findViewById<LinearLayout>(R.id.linearLayout) // Replace with actual ID

                        // Loop through each trip and create a CardView
                        for (trip in trips) {
                            // Inflate the card view
                            val inflater = LayoutInflater.from(this@SendView) // Replace 'YourActivityName' with the name of your activity
                            val tripCard = inflater.inflate(R.layout.trip_card, container, false)

                            // Populate card data
                            tripCard.findViewById<TextView>(R.id.Name).text = trip.loadOwner.toString() // Assuming 'productName' is a field in the 'Trip' class
                            tripCard.findViewById<TextView>(R.id.weight).text = trip.trailer.toString()// Adapt as per your Trip class
                            tripCard.findViewById<TextView>(R.id.pickUpDate).text = trip.status
                            tripCard.findViewById<TextView>(R.id.pickUpAddress).text = trip.pickup.toString()
                            tripCard.findViewById<TextView>(R.id.dropOffDate).text = trip.dropoff.toString()

                            // Add the populated card view to the container
                            container.addView(tripCard)
                        }

                        Toast.makeText(applicationContext, "Succesful", Toast.LENGTH_SHORT).show()
                    } else {
                        // Handle unsuccessful response
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<List<Trip>>, t: Throwable) {
                // Handle failure
            }
        })
    }

}