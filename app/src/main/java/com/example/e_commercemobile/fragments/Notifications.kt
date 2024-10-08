package com.example.e_commercemobile.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commercemobile.R
import com.example.e_commercemobile.adapter.NotificationAdapter
import com.example.e_commercemobile.api.RetrofitInstance
import com.example.e_commercemobile.data.model.Notification

class Notifications : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var notificationList: ArrayList<Notification>
    private lateinit var adapter: NotificationAdapter
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notification, container, false)

        recyclerView = view.findViewById(R.id.notificationRecycler)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        progressBar = view.findViewById(R.id.notificationProgress)

        notificationList = ArrayList()
        adapter = NotificationAdapter(notificationList)
        recyclerView.adapter = adapter

        // Get user id
        val userID = getUserID(requireContext())

        Log.i("Notification", "User ID: $userID")

        //fetch notifications from the API
        fetchNotifications(userID!!)

        return view
    }

    // Get user id from shared preferences
    fun getUserID(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        return sharedPreferences.getString("USER_ID", null)
    }

    fun fetchNotifications(userID: String) {
        progressBar.visibility = View.VISIBLE
        val call = RetrofitInstance.cartApi.getNotification(userID)
        call.enqueue(object : retrofit2.Callback<List<Notification>> {
            override fun onResponse(call: retrofit2.Call<List<Notification>>, response: retrofit2.Response<List<Notification>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val notifications = response.body()
                    if (notifications != null) {
                        notificationList.addAll(notifications)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<List<Notification>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("Notification", "Error: ${t.message}")
            }
        })
    }

}