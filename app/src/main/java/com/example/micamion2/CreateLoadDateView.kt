
package com.example.micamion2

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CreateLoadDateView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_load_date_view)

        val progressbar = findViewById<ProgressBar>(R.id.progressBar)
        val currentprogress = 100
        progressbar.setProgress(currentprogress)
        progressbar.max = 100

        val backButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        // Set an OnClickListener on the button
        backButton.setOnClickListener {
            // Finish the current activity
            finish()
        }

        val buttonNext = findViewById<Button>(R.id.saveButton)
        buttonNext.setOnClickListener {
            val startDateEditText = findViewById<EditText>(R.id.pickUpDate)
            val endDateEditText = findViewById<EditText>(R.id.deliveryDate)
            val startDate = startDateEditText.text.toString()
            val endDate = endDateEditText.text.toString()
            if (isEndDateBeforeStartDate(startDate, endDate)) {
                val sharedPreferences = getSharedPreferences("LoadDetails", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("pickUpDate", endDate)
                editor.putString("dropOffDate", startDate)
                editor.apply()

                val intent = Intent(this, LoadCompleteDetailsView::class.java)
                startActivity(intent)
            } else {
                // Show a toast if endDate is not before startDate
                Toast.makeText(this, "Pickup date must be before dropoff date", Toast.LENGTH_SHORT)
                    .show()
            }
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

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Handle the date selected by the user here
                // monthOfYear starts from 0 (0 for January, 1 for February, and so on)
                val selectedDate = "$year-${monthOfYear + 1}-${dayOfMonth}T00:00"
                val dateEditText: EditText = findViewById(R.id.pickUpDate)
                dateEditText.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.datePicker.minDate = c.timeInMillis
        datePickerDialog.show()

    }

    private fun showDatePickerDialogDelivery() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Handle the date selected by the user here
                // monthOfYear starts from 0 (0 for January, 1 for February, and so on)
                val selectedDate = "$year-${monthOfYear + 1}-${dayOfMonth}T00:00"
                val dateEditText: EditText = findViewById(R.id.deliveryDate)
                dateEditText.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.datePicker.minDate = c.timeInMillis
        datePickerDialog.show()
    }

    private fun isEndDateBeforeStartDate(endDate: String, startDate: String): Boolean {
        val startDateCalendar = Calendar.getInstance()
        val endDateCalendar = Calendar.getInstance()

        val startDateSplit = startDate.split("-")
        val endDateSplit = endDate.split("-")

        val startYear = startDateSplit[0].toInt()
        val startMonth = startDateSplit[1].toInt() - 1 // Months start from 0 in Calendar
        val startDay = startDateSplit[2].substringBefore("T").toInt() // Extract day from date string

        val endYear = endDateSplit[0].toInt()
        val endMonth = endDateSplit[1].toInt() - 1 // Months start from 0 in Calendar
        val endDay = endDateSplit[2].substringBefore("T").toInt() // Extract day from date string

        startDateCalendar.set(startYear, startMonth, startDay)
        endDateCalendar.set(endYear, endMonth, endDay)

        return endDateCalendar.before(startDateCalendar)
    }


}