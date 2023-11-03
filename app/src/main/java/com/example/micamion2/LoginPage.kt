package com.example.micamion2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.util.Log
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

class LoginPage : AppCompatActivity() {

    private val userService = RetrofitInstance.apiUsuario


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)



// Create a Retrofit instance



        val buttonLogin = findViewById<Button>(R.id.loginButton)
        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)


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

        val call = userService.authenticate(username, password)

        val request = call.request()
        val url = request.url().toString()
        call.enqueue(object : Callback<AuthenticationResponse> {
            override fun onResponse(call: Call<AuthenticationResponse>, response: Response<AuthenticationResponse>) {
                if (response.isSuccessful) {
                    val authenticationResponse = response.body()
                    if (authenticationResponse != null) {
                        val token = authenticationResponse.token
                        // Save the token or handle authentication success
                        // For example, you can save it to SharedPreferences for later use
                        // Then proceed to the next screen
                        userService.getUserByEmail(username).enqueue(object : Callback<User> {
                            override fun onResponse(call: Call<User>, response: Response<User>) {
                                if (response.isSuccessful) {
                                    val user = response.body()
                                    if (user != null) {
                                        val userType = user.userType
                                        val name = user.name
                                        val email = user.email
                                        val lastName = user.lastName
                                        val phone = user.phone
                                        if (userType == "LO"){
                                            val intent = Intent(this@LoginPage, ServicesCompanyPersona::class.java)
                                            intent.putExtra("Name", name)
                                            intent.putExtra("Email", email)
                                            intent.putExtra("User Type", userType)
                                            intent.putExtra("Last Name", lastName)
                                            intent.putExtra("Phone", phone)
                                            startActivity(intent)
                                        }
                                        if (userType == "TO"){
                                            val intent = Intent(this@LoginPage, ServicesTruckOwner::class.java)
                                            intent.putExtra("Name", name)
                                            startActivity(intent)

                                        }
                                        else{
                                            Toast.makeText(applicationContext, "Truck driver view", Toast.LENGTH_SHORT).show()
                                        }

                                    } else {
                                        Toast.makeText(applicationContext, "No user found with email: $username", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(applicationContext, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<User>, t: Throwable) {
                                Toast.makeText(applicationContext, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })

                    } else {
                        Toast.makeText(this@LoginPage, "Authentication failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginPage, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AuthenticationResponse>, t: Throwable) {
                Toast.makeText(this@LoginPage, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


