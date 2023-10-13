package com.example.micamion2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
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

        val buttonBack = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        buttonBack.setOnClickListener {
            val intent = Intent(this, SendView::class.java)
            startActivity(intent)
        }

        val buttonNext = findViewById<Button>(R.id.nextButton)
        buttonNext.setOnClickListener {
            val intent = Intent(this, CreateLoadDestinationView::class.java)
            startActivity(intent)
        }

    }
}