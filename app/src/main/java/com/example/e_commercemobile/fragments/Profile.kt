package com.example.e_commercemobile.fragments

import android.content.Intent
import android.os.Bundle
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


class profile : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var deactivateBtn: Button
    private lateinit var logoutBtn: Button
    private lateinit var editProfileBtn: Button
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText


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

        logoutBtn.setOnClickListener{
            val intent = Intent(activity, Login::class.java)
            startActivity(intent)
        }

        deactivateBtn.setOnClickListener{
            Toast.makeText(activity, "Account deactivated Contact CSR to reactivate it", Toast.LENGTH_LONG).show()
            val intent = Intent(activity, Login::class.java)
            startActivity(intent)
        }


        return view
    }


}