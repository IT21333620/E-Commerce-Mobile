package com.example.e_commercemobile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.e_commercemobile.api.RetrofitInstance
import com.example.e_commercemobile.data.model.LoggedInUser
import com.example.e_commercemobile.data.model.LoginRequest

class Login : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var signUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        signUp = findViewById(R.id.signupBtn)

        //Redirect to Register Activity
        signUp.setOnClickListener {
            val intent = Intent(this@Login, Register::class.java)
            startActivity(intent)
        }

        //Login Button
        login.setOnClickListener {

            // Check if username and password are empty
            if (username.text.toString().isEmpty()) {
                username.error = "Please enter username"
                return@setOnClickListener
            }

            if (password.text.toString().isEmpty()) {
                password.error = "Please enter password"
                return@setOnClickListener
            }

            // Make a login request
            val loginRequest = LoginRequest(username.text.toString(), password.text.toString())

            val call = RetrofitInstance.authApi.Login(loginRequest)
            call.enqueue(object : retrofit2.Callback<LoggedInUser> {
                override fun onResponse(call: retrofit2.Call<LoggedInUser>, response: retrofit2.Response<LoggedInUser>) {
                    if (response.isSuccessful) {
                        val loginDetails = response.body()
                        Log.i("CategoryFragment", "Response Body: $loginDetails")

                        if (loginDetails != null) {
                            // Check if account is active
                            if(loginDetails.active_status) {
                                Toast.makeText(
                                    this@Login,
                                    "Account is not active",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return

                            } else {
                                // Check if user is a customer
                                if (loginDetails.role == "Customer") {
                                    Toast.makeText(
                                        this@Login,
                                        "Login Successfully",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    // Save user ID in shared preferences
                                    saveUserID(this@Login, loginDetails.id)

                                    // Redirect to MainActivity
                                    val intent = Intent(this@Login, MainActivity::class.java)
                                    startActivity(intent)

                                } else {
                                    Toast.makeText(
                                        this@Login,
                                        "Only Customers are allowed to login",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(this@Login, "Failed to login", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<LoggedInUser>, t: Throwable) {
                    Toast.makeText(this@Login, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })

        }
    }

    // Save user ID in shared preferences
    fun saveUserID(context: Context, userID: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("USER_ID", userID)
        editor.apply()
    }
}