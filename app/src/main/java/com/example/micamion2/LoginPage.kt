package com.example.micamion2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.micamion2.databinding.ActivityMainBinding

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)



        val buttonLogin = findViewById<Button>(R.id.loginButton)
        buttonLogin.setOnClickListener {
            val intent = Intent(this, ServicesCompanyPersona::class.java)
            startActivity(intent)
        }

        val forgotpasswordtextview = findViewById<TextView>(R.id.forgotPasswordLink)

        forgotpasswordtextview.setOnClickListener {

            val myIntent = Intent(this, ForgotPasswordView::class.java)
            startActivity(myIntent)

            forgotpasswordtextview.movementMethod = LinkMovementMethod.getInstance();

        }

        val createanaccountview = findViewById<TextView>(R.id.createAccountLink)

        createanaccountview.setOnClickListener {

            val myIntent = Intent(this, CreateAccount::class.java)
            startActivity(myIntent)

            createanaccountview.movementMethod = LinkMovementMethod.getInstance();

        }

    }
}