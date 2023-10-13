package com.example.micamion2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
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
    }
}