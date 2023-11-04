package com.example.micamion2

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar

class CreateLoadDateView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_load_date_view)

        val progressbar = findViewById<ProgressBar>(R.id.progressBar)
        val currentprogress = 100
        progressbar.setProgress(currentprogress)
        progressbar.max = 100

        val buttonNext = findViewById<Button>(R.id.saveButton)
        buttonNext.setOnClickListener {
            val intent = Intent(this, CreateLoadDestinationView::class.java)
            startActivity(intent)
        }

        val dateIcon1 = findViewById<ImageView>(R.id.pickUpDateIcon)

        dateIcon1.setOnClickListener {
            showDatePickerDialog()
        }

        val dateIcon2 = findViewById<ImageView>(R.id.deliveryDateIcon)

        dateIcon2.setOnClickListener {
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
                val dateEditText: EditText = findViewById(R.id.pickUpDate)
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
                val dateEditText: EditText = findViewById(R.id.deliveryDate)
                dateEditText.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }
}