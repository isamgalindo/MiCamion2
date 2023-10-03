package com.example.micamion2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.micamion2.databinding.ActivityMainBinding
import android.text.method.LinkMovementMethod
import android.content.Intent


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val forgotpasswordtextview = findViewById<TextView>(R.id.forgotPasswordLink)

        forgotpasswordtextview.setOnClickListener {

            val myIntent = Intent(this, ForgotPasswordView::class.java)
            startActivity(myIntent)

            forgotpasswordtextview.movementMethod = LinkMovementMethod.getInstance();
        }

        binding.loginButton.setOnClickListener(View.OnClickListener {
            if(binding.username.text.toString() == "user" && binding.password.text.toString() == "1234"){
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT ).show()
            } else{

                Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT ).show()

            }
        })

    }
}