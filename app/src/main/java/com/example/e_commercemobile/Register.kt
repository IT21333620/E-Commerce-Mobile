package com.example.e_commercemobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.e_commercemobile.api.RetrofitInstance
import com.example.e_commercemobile.data.model.LoggedInUser
import com.example.e_commercemobile.data.model.RegisterRequest
import com.example.e_commercemobile.databinding.ActivityRegisterBinding


class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val register = binding.signUpBtn
        val login = binding.signInBtn
        val name = binding.signUpNameTxt
        val email = binding.signUpEmailTxt
        val password = binding.signupPassTxt
        val confirmPassword = binding.signUpConfirmTxt

        login.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        register.setOnClickListener {
            if(name.text.toString().isEmpty()){
                name.error = "Please enter your name"
                return@setOnClickListener
            }

            if(email.text.toString().isEmpty()){
                email.error = "Please enter your email"
                return@setOnClickListener
            }

            if(password.text.toString().isEmpty()){
                password.error = "Please enter your password"
                return@setOnClickListener
            }

            if (checkPassword(password.text.toString(), confirmPassword.text.toString())) {

                val registerRequest = RegisterRequest(name.text.toString(), email.text.toString(), password.text.toString(), "Customer")

                val call = RetrofitInstance.authApi.Register(registerRequest)
                call.enqueue(object : retrofit2.Callback<LoggedInUser> {
                    override fun onResponse(call: retrofit2.Call<LoggedInUser>, response: retrofit2.Response<LoggedInUser>) {
                        if (response.isSuccessful) {
                            val registerDetails = response.body()
                            Log.i("CategoryFragment", "Response Body: $registerDetails")

                            if (registerDetails != null) {
                                val toast = Toast.makeText(applicationContext, "Registration Successful", Toast.LENGTH_SHORT)
                                toast.show()
                            }
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<LoggedInUser>, t: Throwable) {
                        Log.e("CategoryFragment", "Error: ${t.message}")
                    }
                })


                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                val toast = Toast.makeText(applicationContext, "Passwords do not match", Toast.LENGTH_SHORT)
                toast.show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    private fun checkPassword(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }
}