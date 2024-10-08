package com.example.e_commercemobile.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commercemobile.R
import com.example.e_commercemobile.adapter.OrderAdapter
import com.example.e_commercemobile.api.RetrofitInstance
import com.example.e_commercemobile.data.model.OrderHistory

class history : Fragment() {

   private lateinit var recyclerView: RecyclerView
   private lateinit var historyList: ArrayList<OrderHistory>
   private lateinit var adapter: OrderAdapter
   private lateinit var progressBar: ProgressBar
   private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_history, container, false)

        recyclerView = view.findViewById(R.id.histroyRecycleView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        progressBar = view.findViewById(R.id.progressBarHistory)
        imageView = view.findViewById(R.id.notificationIcon)


        historyList = ArrayList()

        // Get user id
        val userID = getUserID(requireContext())

        //Fetch order history from the API
        progressBar.visibility = View.VISIBLE
        val call = RetrofitInstance.cartApi.getOrderHistory(userID!!)
        call.enqueue(object : retrofit2.Callback<List<OrderHistory>> {
            override fun onResponse(call: retrofit2.Call<List<OrderHistory>>, response: retrofit2.Response<List<OrderHistory>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val orderHistory = response.body()
                    if (orderHistory != null) {
                        historyList.addAll(orderHistory)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<List<OrderHistory>>, t: Throwable) {
                println("Error: ${t.message}")
                progressBar.visibility = View.GONE
            }
        })

        adapter = OrderAdapter(historyList)
        recyclerView.adapter = adapter

        imageView.setOnClickListener{
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, Notifications())
            fragmentTransaction?.commit()
        }


        return view
    }

    // Get user id from shared preferences
    fun getUserID(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        return sharedPreferences.getString("USER_ID", null)
    }
}