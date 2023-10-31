package com.example.micamion2

import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast

class CreateLoadDestinationView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_load_destination_view)

        val progressbar = findViewById<ProgressBar>(R.id.progressBar)
        val currentprogress = 50
        progressbar.setProgress(currentprogress)
        progressbar.max = 100
        val buttonNext = findViewById<Button>(R.id.nextButton)
        buttonNext.setOnClickListener {
            val intent = Intent(this, CreateLoadDateView::class.java)
            startActivity(intent)
        }

        val editText = findViewById<EditText>(R.id.destination)
        val searchIcon = findViewById<ImageView>(R.id.searchIcon1)
        val searchIcon2 = findViewById<ImageView>(R.id.searchIcon2)

        searchIcon.setOnClickListener {
            val intent = Intent(this, FuncAF::class.java)
            startActivity(intent)
            // Handle the click on the icon here
            // You can open a search dialog or perform any action when the icon is clicked
        }

        searchIcon2.setOnClickListener {
            val intent = Intent(this, FuncAF::class.java)
            startActivity(intent)
            // Handle the click on the icon here
            // You can open a search dialog or perform any action when the icon is clicked
        }
    }
}