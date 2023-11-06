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
import androidx.cardview.widget.CardView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CreateLoadDetailsView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_load_details_view)


        val progressbar = findViewById<ProgressBar>(R.id.progressBar)
        val currentprogress = 20
        progressbar.setProgress(currentprogress)
        progressbar.max = 100

        val buttonNext = findViewById<Button>(R.id.nextButton)
        buttonNext.setOnClickListener {
            val nameLoadEditText = findViewById<EditText>(R.id.loadName)
            val typeLoadEditText = findViewById<EditText>(R.id.loadType)
            val weightTypeEditText = findViewById<EditText>(R.id.loadWeight)
            val volumeTypeEditText = findViewById<EditText>(R.id.loadVolume)

            val nameLoad = nameLoadEditText.text.toString()
            val typeLoad = typeLoadEditText.text.toString()
            val weightLoad = weightTypeEditText.text.toString()
            val volumeLoad = volumeTypeEditText.text.toString()

            val sharedPreferences = getSharedPreferences("LoadDetails", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
                editor.putString("nameLoad", nameLoad)
                editor.putString("typeLoad", typeLoad)
                editor.putString("weightLoad", weightLoad)
                editor.putString("volumeLoad", volumeLoad)
                editor.apply()  // Or use commit() if you need synchronous storage


            val intent = Intent(this, CreateLoadDestinationView::class.java)
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
                Toast.makeText(this@CreateLoadDetailsView, "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this@CreateLoadDetailsView, "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

    }
}