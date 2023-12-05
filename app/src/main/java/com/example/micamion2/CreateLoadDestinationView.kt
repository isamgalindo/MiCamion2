package com.example.micamion2

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateLoadDestinationView : AppCompatActivity() {

    private val userService = RetrofitInstance.apiUsuario
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_load_destination_view)

        val backButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        // Set an OnClickListener on the button
        backButton.setOnClickListener {
            // Finish the current activity
            finish()
        }

        val progressbar = findViewById<ProgressBar>(R.id.progressBar)
        val currentprogress = 50
        progressbar.setProgress(currentprogress)
        progressbar.max = 100
        val buttonNext = findViewById<Button>(R.id.nextButton)
        buttonNext.setOnClickListener {
            val pickUpEditText = findViewById<EditText>(R.id.pickUpLocation)
            val dropOffEditText = findViewById<EditText>(R.id.destination)
            val recipientNameEditText = findViewById<EditText>(R.id.recipientName)
            val recipientPhoneEditText = findViewById<EditText>(R.id.recipientPhone)


            val recipientPhone = recipientPhoneEditText.text.toString()


            val recipientMail = recipientNameEditText.text.toString()

            val progressDialog = ProgressDialog(this@CreateLoadDestinationView)
            progressDialog.setMessage("Getting recepient...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            CoroutineScope(Dispatchers.IO).launch {
                val call = userService.getUserByEmail(recipientMail)
                try {
                    val response = call.execute()

                    if (response.isSuccessful) {
                        val recepientId = response.body()
                        if (recepientId != null) {

                            withContext(Dispatchers.Main) {

                                val sharedPreferences = getSharedPreferences("LoadDetails", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()

                                editor.putString("recipientMail", recipientMail)

                                editor.apply()
                                val intent = Intent(this@CreateLoadDestinationView, CreateLoadDateView::class.java)
                                startActivity(intent)


                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                progressDialog.dismiss()
                                Toast.makeText(this@CreateLoadDestinationView, "This recepient user doesn't exist", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            progressDialog.dismiss()
                            Toast.makeText(this@CreateLoadDestinationView, "This recepient user doesn't exist", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        progressDialog.dismiss()
                        Toast.makeText(this@CreateLoadDestinationView, "No internet connection", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
        val searchIcon = findViewById<ImageView>(R.id.searchIcon1)
        val searchIcon2 = findViewById<ImageView>(R.id.searchIcon2)
        val pickUpEditText = findViewById<EditText>(R.id.pickUpLocation)
        val dropOffEditText = findViewById<EditText>(R.id.destination)

        val sharedPreferences = getSharedPreferences("LoadDetails", Context.MODE_PRIVATE)
        pickUpEditText.setText(""+sharedPreferences.getString("originAddress","")+""+sharedPreferences.getString("originCity","")+""+sharedPreferences.getString("originCountry",""))
        dropOffEditText.setText(""+sharedPreferences.getString("destinationAddress","")+""+sharedPreferences.getString("destinationCity","")+""+sharedPreferences.getString("destinationCountry",""))


        searchIcon.setOnClickListener {
            val intent = Intent(this, OriginMap::class.java)
            startActivity(intent)
            pickUpEditText.setText(""+sharedPreferences.getString("originAddress","")+""+sharedPreferences.getString("originCity","")+""+sharedPreferences.getString("originCountry",""))

            val sharedPreferences = getSharedPreferences("LoadDetails", Context.MODE_PRIVATE)
            val originCity = sharedPreferences.getString("originCity", "")
            // Check if we actually have a location saved (i.e., not 0f for both)
            if (originCity == null ) {

                Toast.makeText(this, "No location saved", Toast.LENGTH_SHORT).show()
            }
        }

        searchIcon2.setOnClickListener {
            val intent = Intent(this, DestinationMap::class.java)
            startActivity(intent)
            dropOffEditText.setText(""+sharedPreferences.getString("destinationAddress","")+""+sharedPreferences.getString("destinationCity","")+""+sharedPreferences.getString("destinationCountry",""))

            val sharedPreferences = getSharedPreferences("LoadDetails", Context.MODE_PRIVATE)
            val destinationCity = sharedPreferences.getString("destinationCity", "")
            // Check if we actually have a location saved (i.e., not 0f for both)
            if (destinationCity == null ) {

                Toast.makeText(this, "No location saved", Toast.LENGTH_SHORT).show()
            }
            else{

            }

        }
    }
}