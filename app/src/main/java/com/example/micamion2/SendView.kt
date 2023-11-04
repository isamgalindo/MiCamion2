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

class SendView : AppCompatActivity() {
    private lateinit var searchView: SearchView
    private lateinit var container: LinearLayout
    private lateinit var allCardViews: List<CardView>
    private var name =""
    private var email =""
    private var userType =""
    private var lastName =""
    private var phone =""
    private val userService = RetrofitInstance.apiUsuario
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_view)

        val intent = intent
        name = intent.getStringExtra("Name").toString()
        email = intent.getStringExtra("Email").toString()
        userType = intent.getStringExtra("User Type").toString()
        lastName = intent.getStringExtra("Last Name").toString()
        phone = intent.getStringExtra("Phone").toString()



        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.loadmenu
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.loadmenu -> {
                    true
                }
                R.id.home ->{
                    val intent = Intent(this@SendView, ServicesCompanyPersona::class.java)
                    intent.putExtra("Name", name)
                    intent.putExtra("Email", email)
                    intent.putExtra("User Type", userType)
                    intent.putExtra("Last Name", lastName)
                    intent.putExtra("Phone", phone)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.profile ->{
                    val intent = Intent(this@SendView, ProfileView::class.java)
                    intent.putExtra("Name", name)
                    intent.putExtra("Email", email)
                    intent.putExtra("User Type", userType)
                    intent.putExtra("Last Name", lastName)
                    intent.putExtra("Phone", phone)
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
        //Takes all the trips to put it in the Loads view
        userService.getAllTrips().enqueue(object : Callback<List<Trip>> {
            override fun onResponse(call: Call<List<Trip>>, response: Response<List<Trip>>) {
                if (response.isSuccessful) {
                    val trips = response.body()
                    if (trips != null) {
                        // Get reference to the LinearLayout container
                        val container = findViewById<LinearLayout>(R.id.linearLayout)

                        trips.forEach { trip ->
                            val inflater = LayoutInflater.from(this@SendView)
                            val tripCard = inflater.inflate(R.layout.trip_card, container, false).apply {
                                findViewById<TextView>(R.id.status).text = trip.status
                                container.addView(this)
                            }
                            //Takes the type and weight from load
                            userService.getLoadById(trip.load).enqueue(object : Callback<Load> {
                                override fun onResponse(call: Call<Load>, response: Response<Load>) {
                                    if (response.isSuccessful) {
                                        val load = response.body()
                                        if (load != null) {
                                            tripCard.findViewById<TextView>(R.id.Name).text = load.type
                                            tripCard.findViewById<TextView>(R.id.weight).text = load.weight.toString()
                                        } else {
                                            Toast.makeText(applicationContext, "LOADS!", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Toast.makeText(applicationContext, "loadsErr: ${response.code()}", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<Load>, t: Throwable) {
                                    Toast.makeText(applicationContext, "loadsFailure: ${t.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                            //Takes the city, address and country from access points
                            userService.getAccessPointById(trip.pickup).enqueue(object : Callback<AccessPoint> {
                                override fun onResponse(call: Call<AccessPoint>, response: Response<AccessPoint>) {
                                    if (response.isSuccessful) {
                                        val ap = response.body()
                                        if (ap != null) {
                                            tripCard.findViewById<TextView>(R.id.pickUpAddress).text = "${ap.address}, ${ap.city}, ${ap.country}"
                                            Toast.makeText(applicationContext, "AP!", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(applicationContext, "AP!", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Toast.makeText(applicationContext, "${trip.pickup}: ${response.code()}", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<AccessPoint>, t: Throwable) {
                                    Toast.makeText(applicationContext, "APFailure: ${t.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                            userService.getAccessPointById(trip.dropoff).enqueue(object : Callback<AccessPoint> {
                                override fun onResponse(call: Call<AccessPoint>, response: Response<AccessPoint>) {
                                    if (response.isSuccessful) {
                                        val ap = response.body()
                                        if (ap != null) {
                                            tripCard.findViewById<TextView>(R.id.dropOffAddress).text = "${ap.address}, ${ap.city}, ${ap.country}"
                                            Toast.makeText(applicationContext, "AP!", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(applicationContext, "AP!", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Toast.makeText(applicationContext, "${trip.dropoff}: ${response.code()}", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<AccessPoint>, t: Throwable) {
                                    Toast.makeText(applicationContext, "APFailure: ${t.message}", Toast.LENGTH_SHORT).show()
                                }
                            })

                        }
                    } else {
                        Toast.makeText(applicationContext, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Trip>>, t: Throwable) {
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