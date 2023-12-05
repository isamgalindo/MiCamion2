package com.example.micamion2

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.AdapterView

class CreateTruckDetails : AppCompatActivity() {
    private val userService = RetrofitInstance.apiUsuario
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_truck_details)
        val backButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)

        // Set an OnClickListener on the button
        backButton.setOnClickListener {
            // Finish the current activity
            finish()
        }


        val progressbar = findViewById<ProgressBar>(R.id.progressBar)
        val currentprogress = 33
        progressbar.setProgress(currentprogress)
        progressbar.max = 100

        val buttonNext = findViewById<Button>(R.id.nextButton)
        buttonNext.setOnClickListener {
            val modelEditText = findViewById<EditText>(R.id.model)
            val brandEditText = findViewById<EditText>(R.id.brand)
            val platesEditText = findViewById<EditText>(R.id.plates)
            val weightEditText = findViewById<EditText>(R.id.truckWeight)
            val volumeEditText = findViewById<EditText>(R.id.truckVolume)
            val driverEditText = findViewById<EditText>(R.id.driver)

            val model = modelEditText.text.toString()
            val brand = brandEditText.text.toString()
            val plates = platesEditText.text.toString()
            val weight = weightEditText.text.toString()
            val volume = volumeEditText.text.toString()
            val driver = driverEditText.text.toString()

            val isWeightNumeric = weight.toDoubleOrNull() != null
            val isVolumeNumeric = volume.toDoubleOrNull() != null

            if (model.isNotBlank() && brand.isNotBlank() && plates.length == 6 && weight.isNotBlank()
                && volume.isNotBlank() && driver.isNotBlank() && isWeightNumeric && isVolumeNumeric) {

                val progressDialog = ProgressDialog(this@CreateTruckDetails)
                progressDialog.setMessage("Getting driver...")
                progressDialog.setCancelable(false)
                progressDialog.show()

                CoroutineScope(Dispatchers.IO).launch {
                    val call = userService.getUserByEmail(driver)
                    try {
                        val response = call.execute()

                        if (response.isSuccessful) {
                            val driver = response.body()
                            if (driver != null) {
                                withContext(Dispatchers.Main) {
                                    val sharedPreferences = getSharedPreferences("TruckDetails", Context.MODE_PRIVATE)
                                    val editor = sharedPreferences.edit()
                                    editor.putString("modelTruck", model)
                                    editor.putString("brandTruck", brand)
                                    editor.putString("platesTruck", plates)
                                    editor.putString("weightTruck", weight)
                                    editor.putString("volumeTruck", volume)
                                    editor.putInt("driverTruck", driver.id)
                                    editor.apply()  // Or use commit() if you need synchronous storage

                                    val intent = Intent(this@CreateTruckDetails, CreateTruckLocation::class.java)
                                    startActivity(intent)


                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    progressDialog.dismiss()
                                    Toast.makeText(this@CreateTruckDetails, "This driver user doesn't exist", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                progressDialog.dismiss()
                                Toast.makeText(this@CreateTruckDetails, "This driver user doesn't exist", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            progressDialog.dismiss()
                            Toast.makeText(this@CreateTruckDetails, "No internet Connection", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                // All fields are filled, and the plates field is 6 characters long

            } else {
                // Show toast if any field is empty or plates field length is not 6 characters
                if (plates.length != 6) {
                    Toast.makeText(this@CreateTruckDetails, "Please enter 6 characters for Plates", Toast.LENGTH_SHORT).show()
                } else if (!isWeightNumeric || !isVolumeNumeric) {
                    Toast.makeText(this@CreateTruckDetails, "Please enter numeric values for Weight and Volume", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@CreateTruckDetails, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val items = arrayOf("Kg", "g", "Lb","Tons")
        val adapter = ArrayAdapter(this@CreateTruckDetails, android.R.layout.simple_spinner_dropdown_item, items)
        val spinner: Spinner = findViewById(R.id.weightSpinner)
        spinner.adapter = adapter


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                // Do something with selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        val itemsVol = arrayOf("Lt", "cm3", "m3")
        val adapterVol = ArrayAdapter(this@CreateTruckDetails, android.R.layout.simple_spinner_dropdown_item, itemsVol)
        val spinnerVol: Spinner = findViewById(R.id.volumeSpinner)
        spinnerVol.adapter = adapterVol

        spinnerVol.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                // Do something with selected item
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        val itemsType = arrayOf("Flatbed", "Dry Van", "Reefer", "Lowboy","Stepdeck", "Other", "Any")
        val adapterType = ArrayAdapter(this@CreateTruckDetails, android.R.layout.simple_spinner_dropdown_item, itemsType)
        val spinnerType: Spinner = findViewById(R.id.typeSpinner)
        spinnerType.adapter = adapterType

        spinnerVol.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                // Do something with selected item
                val sharedPreferences = getSharedPreferences("TruckDetails", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("typeTruck", selectedItem)
                editor.apply()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }
}