package com.example.e_commercemobile.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commercemobile.R
import com.example.e_commercemobile.adapter.CartAdapter
import com.example.e_commercemobile.api.RetrofitInstance
import com.example.e_commercemobile.data.model.Cart
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.ResponseBody

class cart : Fragment() {

    private lateinit var checkOut: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartITemsList: ArrayList<Cart>
    private lateinit var adapter: CartAdapter
    private lateinit var totalPriceTextView: TextView
    private lateinit var cancleButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        recyclerView = view.findViewById(R.id.cartRecycleview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        totalPriceTextView = view.findViewById(R.id.totalPriceText)
        cancleButton = view.findViewById(R.id.clearCartButton)

        cartITemsList = ArrayList()

        // Get user id
        val userID = getUserID(requireContext())

        // Fetch products from the API
        val call = RetrofitInstance.cartApi.getCartItems(userID!!)
        call.enqueue(object : retrofit2.Callback<List<Cart>> {
            override fun onResponse(call: retrofit2.Call<List<Cart>>, response: retrofit2.Response<List<Cart>>) {
                if (response.isSuccessful) {
                    val cartItems = response.body()
                    if (cartItems != null) {
                        cartITemsList.addAll(cartItems)
                        adapter.notifyDataSetChanged()
                        updateTotalPrice()
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<List<Cart>>, t: Throwable) {
                println("Error: ${t.message}")
            }
        })

        adapter = CartAdapter(cartITemsList){
            updateTotalPrice()
        }

        recyclerView.adapter = adapter

        checkOut = view.findViewById(R.id.checkOutFloatingBtn)

        checkOut.setOnClickListener{
            val selectedItems = adapter.getSelectedItems()
            val fragment = CheckOut()

            val bundle = Bundle()
            bundle.putParcelableArrayList("selectedItems", ArrayList(selectedItems))
            fragment.arguments = bundle

            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, fragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }

        cancleButton.setOnClickListener {
            val call = RetrofitInstance.cartApi.clearCart(userID!!)
            call.enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: retrofit2.Call<ResponseBody>,
                    response: retrofit2.Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        cartITemsList.clear()
                        adapter.notifyDataSetChanged()
                        updateTotalPrice()

                        val toast = Toast.makeText(requireContext(), "Cart cleared", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                   val toast = Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT)
                    toast.show()
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

    // Update total price
    private fun updateTotalPrice() {
        val totalPrice = adapter.getTotalPrice()
        totalPriceTextView.text = "$$totalPrice"
    }


}