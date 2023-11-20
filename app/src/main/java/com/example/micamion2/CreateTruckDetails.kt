package com.example.micamion2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast

class CreateTruckDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_truck_details)

        val progressbar = findViewById<ProgressBar>(R.id.progressBar)
        val currentprogress = 20
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

            val sharedPreferences = getSharedPreferences("TruckDetails", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("modelTruck", model)
            editor.putString("brandTruck", brand)
            editor.putString("platesTruck", plates)
            editor.putString("weightTruck", weight)
            editor.putString("volumeTruck", volume)
            editor.putString("driverTruck", driver)
            editor.apply()  // Or use commit() if you need synchronous storage

            val intent = Intent(this, CreateTruckLocation::class.java)
            startActivity(intent)
        }

        val items = arrayOf("Kg", "g", "Lb","Tons")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        val spinner: Spinner = findViewById(R.id.weightSpinner)
        spinner.adapter = adapter


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                // Do something with selected item
                Toast.makeText(this@CreateTruckDetails, "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        val itemsVol = arrayOf("Lt", "cm3", "m3")
        val adapterVol = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, itemsVol)
        val spinnerVol: Spinner = findViewById(R.id.volumeSpinner)
        spinnerVol.adapter = adapterVol

        spinnerVol.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                // Do something with selected item
                Toast.makeText(this@CreateTruckDetails, "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        val itemsType = arrayOf("Flatbed", "Dry Van", "Reefer", "Lowboy","Stepdeck", "Other", "Any")
        val adapterType = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, itemsType)
        val spinnerType: Spinner = findViewById(R.id.typeSpinner)
        spinnerType.adapter = adapterType

        spinnerVol.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                // Do something with selected item
                Toast.makeText(this@CreateTruckDetails, "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }
}