package com.example.micamion2

import android.content.Context
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.micamion2.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.*


class LoginPage : AppCompatActivity() {

    private val userService = RetrofitInstance.apiUsuario


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)





// Create a Retrofit instance



        val buttonLogin = findViewById<Button>(R.id.loginButton)
        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)

// Set up touch listener for non-text box views to hide keyboard.
        passwordEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (passwordEditText.right - passwordEditText.compoundDrawables[2].bounds.width())) {
                    // Toggle password visibility
                    if (passwordEditText.transformationMethod == PasswordTransformationMethod.getInstance()) {
                        passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_24, 0)
                    } else {
                        passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                        passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_24, 0)
                    }
                    // Move the cursor to the end of the text
                    passwordEditText.setSelection(passwordEditText.text.length)
                    return@setOnTouchListener true
                }
            }
            false
        }


        buttonLogin.setOnClickListener {

            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()



            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                // One or both input fields are empty, display an error message or handle it as needed
                // For example, display a toast message:
                Toast.makeText(this, "Both username and password are required.", Toast.LENGTH_SHORT).show()
            } else {

                authenticateUser(username, password);
                // Both input fields have values, proceed with the login logic
                //val intent = Intent(this, ServicesCompanyPersona::class.java)
                //startActivity(intent)
            }

        }

        val forgotpasswordtextview = findViewById<TextView>(R.id.forgotPasswordLink)

        forgotpasswordtextview.setOnClickListener {

            val myIntent = Intent(this, ForgotPasswordView::class.java)
            startActivity(myIntent)

            forgotpasswordtextview.movementMethod = LinkMovementMethod.getInstance()

        }

        val createanaccountview = findViewById<TextView>(R.id.createAccountLink)

        createanaccountview.setOnClickListener {

            val myIntent = Intent(this, LikeToDo::class.java)
            startActivity(myIntent)

            createanaccountview.movementMethod = LinkMovementMethod.getInstance()

        }


    }



    private fun authenticateUser(username: String, password: String) {
        val progressDialog = ProgressDialog(this@LoginPage)
        progressDialog.setMessage("Authenticating...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        CoroutineScope(Dispatchers.IO).launch {
            val call = userService.authenticate(username, password)
            try {
                val response = call.execute()

                if (response.isSuccessful) {
                    val authenticationResponse = response.body()
                    if (authenticationResponse != null) {
                        val token = authenticationResponse.token
                        // Save the token or handle authentication success
                        // ...
                        // Proceed to next screen
                        withContext(Dispatchers.Main) {
                            userService.getUserByEmail(username).enqueue(object : Callback<User> {
                                override fun onResponse(call: Call<User>, response: Response<User>) {
                                    progressDialog.dismiss()
                                    if (response.isSuccessful) {

                                        val user = response.body()

                                        if (user != null) {

                                            val userType = user.userType
                                            val name = user.name
                                            val email = user.email
                                            val lastName = user.last_name
                                            val phone = user.phone
                                            val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
                                            with(sharedPref.edit()) {
                                                putString("userType", user.userType)
                                                putString("name", user.name)
                                                putString("email", user.email)
                                                putString("lastName", user.last_name)
                                                putString("phone", user.phone)
                                                apply()  // Or use commit() if you need synchronous storage
                                            }
                                            if (userType == "LO"){
                                                val intent = Intent(this@LoginPage, ServicesCompanyPersona::class.java)
                                                startActivity(intent)
                                            }
                                            if (userType == "TO"){
                                                val intent = Intent(this@LoginPage, ServicesTruckOwner::class.java)
                                                startActivity(intent)

                                            }
                                            if (userType == "DR"){
                                                val intent = Intent(this@LoginPage, ServicesDriver::class.java)
                                                startActivity(intent)

                                            }

                                        } else {
                                            Toast.makeText(applicationContext, "No user found with email: $username", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Toast.makeText(
                                            applicationContext,
                                            "Error: ${response.code()}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                override fun onFailure(call: Call<User>, t: Throwable) {
                                    progressDialog.dismiss()
                                    Toast.makeText(applicationContext, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            progressDialog.dismiss()
                            Toast.makeText(this@LoginPage, "Your email and password doesn't match", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        progressDialog.dismiss()
                        Toast.makeText(this@LoginPage, "Your email and password doesn't match", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressDialog.dismiss()
                    Toast.makeText(this@LoginPage, "Error connecting to the server", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


