package com.example.micamion2

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class CreateAccount : AppCompatActivity() {

    private var userType =""
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

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

        val confirmPasswordEditText = findViewById<EditText>(R.id.confirmPassword)

// Set up touch listener for non-text box views to hide keyboard.
        confirmPasswordEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (confirmPasswordEditText.right - confirmPasswordEditText.compoundDrawables[2].bounds.width())) {
                    // Toggle password visibility
                    if (confirmPasswordEditText.transformationMethod == PasswordTransformationMethod.getInstance()) {
                        confirmPasswordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        confirmPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_24, 0)
                    } else {
                        confirmPasswordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                        confirmPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_24, 0)
                    }
                    // Move the cursor to the end of the text
                    confirmPasswordEditText.setSelection(confirmPasswordEditText.text.length)
                    return@setOnTouchListener true
                }
            }
            false
        }

        val intent = intent
        userType = intent.getStringExtra("User Type").toString() // Replace "key" with the key you used in putExtra()

        val buttonSignUp = findViewById<Button>(R.id.SignUpButton)
        val editTextName = findViewById<EditText>(R.id.name)
        val editTextLastName = findViewById<EditText>(R.id.lastName)
        val editTextEmail = findViewById<EditText>(R.id.email)
        val editTextPassword = findViewById<EditText>(R.id.password)
        val editTextConfirmPassword = findViewById<EditText>(R.id.confirmPassword)
        val editTextPhone = findViewById<EditText>(R.id.phoneNumber)


        val loginPageView = findViewById<TextView>(R.id.alreadyHaveAnAccount)

        loginPageView.setOnClickListener {

            val myIntent = Intent(this, LoginPage::class.java)
            startActivity(myIntent)

            loginPageView.movementMethod = LinkMovementMethod.getInstance()

        }

        buttonSignUp.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password1 = editTextPassword.text.toString()
            val password2 = editTextConfirmPassword.text.toString()
            val name = editTextName.text.toString()
            val lastName = editTextLastName.text.toString()
            val phone = editTextPhone.text.toString()

            if (password1 != password2) {
                Toast.makeText(this@CreateAccount, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a CreateUserRequest object
            val createUserRequest = CreateUserRequest(
                email = email,
                password1 = password1,
                password2 = password2,
                name = name,
                lastName = lastName,
                userType = userType,
                phone = phone
            )

            // Call API to create user
            val call = RetrofitInstance.apiUsuario.createUser(createUserRequest)


            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val createdUser = response.body()
                        Toast.makeText(this@CreateAccount, "User created successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@CreateAccount, LoginPage::class.java)
                        // If you need to pass data to the new view, you can use intent.putExtra()

                        startActivity(intent)
                    } else {
                        val errorBody = response.errorBody()?.string()
                        println("$errorBody")
                        Toast.makeText(this@CreateAccount, "Error creating user, $errorBody", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@CreateAccount, "Error conecting to the server", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
