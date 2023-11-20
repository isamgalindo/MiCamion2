package com.example.micamion2

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar

class CreateTruckDate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_truck_date)


        val progressbar = findViewById<ProgressBar>(R.id.progressBar)
        val currentprogress = 100
        progressbar.setProgress(currentprogress)
        progressbar.max = 100

        val buttonSave = findViewById<Button>(R.id.saveButton)
        buttonSave.setOnClickListener {
            val startDateEditText = findViewById<EditText>(R.id.startDate)
            val endDateEditText = findViewById<EditText>(R.id.endDate)

            val startDate = startDateEditText.text.toString()
            val endDate = endDateEditText.text.toString()
            val sharedPreferences = getSharedPreferences("TruckDetails", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("startDate", startDate)
            editor.putString("endDate", endDate)
            editor.apply()
            val intent = Intent(this, TruckCompleteDetails::class.java)
            startActivity(intent)
        }

        val startDate = findViewById<ImageView>(R.id.startDateIcon)

        startDate.setOnClickListener {
            showDatePickerDialog()
        }

        val endDate = findViewById<ImageView>(R.id.endDateIcon)

        endDate.setOnClickListener {
            showDatePickerDialogDelivery()
        }
    }
    private fun showDatePickerDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Handle the date selected by the user here
                // monthOfYear starts from 0 (0 for January, 1 for February, and so on)
                val selectedDate = "${dayOfMonth}/${monthOfYear + 1}/$year"
                val dateEditText: EditText = findViewById(R.id.startDate)
                dateEditText.setText(selectedDate)

            },
            year, month, day
        )
        datePickerDialog.show()

    }

    private fun showDatePickerDialogDelivery() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Handle the date selected by the user here
                // monthOfYear starts from 0 (0 for January, 1 for February, and so on)
                val selectedDate = "${dayOfMonth}/${monthOfYear + 1}/$year"
                val dateEditText: EditText = findViewById(R.id.endDate)
                dateEditText.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }
}