package com.example.e_commercemobile.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.e_commercemobile.Login
import com.example.e_commercemobile.R
import com.example.e_commercemobile.Register
import com.example.e_commercemobile.api.RetrofitInstance
import com.example.e_commercemobile.data.model.LoggedInUser
import okhttp3.ResponseBody
import kotlin.math.log


class profile : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var deactivateBtn: Button
    private lateinit var logoutBtn: Button
    private lateinit var editProfileBtn: Button
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText

    private var loggedInUser: LoggedInUser? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        deactivateBtn = view.findViewById(R.id.deactivateBtn)
        logoutBtn = view.findViewById(R.id.logoutBtn)
        editProfileBtn = view.findViewById(R.id.editBtn)
        nameEditText = view.findViewById(R.id.editNameTxt)
        emailEditText = view.findViewById(R.id.emailEditTxt)

        // Get user id
        val userID = getUserID(requireContext())

        logoutBtn.setOnClickListener{
            // Clear the saved user ID
            val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(activity, Login::class.java)
            startActivity(intent)
        }

        //set user details for profile
        val call = RetrofitInstance.authApi.getUserById(userID!!)
        call.enqueue(object : retrofit2.Callback<LoggedInUser> {
            override fun onResponse(call: retrofit2.Call<LoggedInUser>, response: retrofit2.Response<LoggedInUser>) {
                if (response.isSuccessful) {
                    loggedInUser = response.body()
                    nameEditText.setText(loggedInUser?.name)
                    emailEditText.setText(loggedInUser?.email)
                }
            }

            override fun onFailure(call: retrofit2.Call<LoggedInUser>, t: Throwable) {
                println("Error: ${t.message}")
            }
        })

        editProfileBtn.setOnClickListener{

            if (nameEditText.text.isEmpty()) {
                nameEditText.error = "Name is required"
                return@setOnClickListener
            }

            if (emailEditText.text.isEmpty()) {
                emailEditText.error = "Email is required"
                return@setOnClickListener
            }

            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            loggedInUser?.let {
                it.name = name
                it.email = email

            val updateCall = RetrofitInstance.authApi.updateUser(userID, it)
                updateCall.enqueue(object : retrofit2.Callback<LoggedInUser> {
                override fun onResponse(call: retrofit2.Call<LoggedInUser>, response: retrofit2.Response<LoggedInUser>) {
                    Log.i("ProfileFragment", "Response Body: ${it}")
                    if (response.isSuccessful) {
                        val user = response.body()
                        Toast.makeText(activity, "Profile updated successfully", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<LoggedInUser>, t: Throwable) {
                    println("Error: ${t.message}")
                }
            })
            }
        }

        deactivateBtn.setOnClickListener {
            val deactivateCall = RetrofitInstance.authApi.deactivateUser(userID)
            deactivateCall.enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(call: retrofit2.Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                    Log.i("ProfileFragment", "Response Code: ${response.code()}")
                    if (response.isSuccessful) {
                        Log.i("ProfileFragment", "Deactivation successful")
                        Toast.makeText(requireContext(), "Account deactivated. Contact CSR to reactivate it", Toast.LENGTH_LONG).show()
                        val intent = Intent(requireActivity(), Login::class.java)
                        startActivity(intent)
                    } else {
                        Log.e("ProfileFragment", "Deactivation failed: ${response.errorBody()?.string()}")
                        Toast.makeText(requireContext(), "Failed to deactivate account", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                    Log.e("ProfileFragment", "Error: ${t.message}")
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }


        return view
    }


    // Get user id from shared preferences
    fun getUserID(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        return sharedPreferences.getString("USER_ID", null)
    }




}