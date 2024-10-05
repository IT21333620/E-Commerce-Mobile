package com.example.e_commercemobile.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.e_commercemobile.R
import com.example.e_commercemobile.api.RetrofitInstance
import com.example.e_commercemobile.data.model.AddToCartRequest
import com.example.e_commercemobile.data.model.OrderItem
import com.example.e_commercemobile.data.model.Product

class SingleProductView : Fragment() {

    private var productId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productId = it.getString("product_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_single_product_view, container, false)

        val itemTitle = view.findViewById<TextView>(R.id.ItemTitleSingle)
        val itemPrice = view.findViewById<TextView>(R.id.ItemPriceSingle)
        val itemVendor = view.findViewById<TextView>(R.id.itemVenderSingle)
        val itemCategory = view.findViewById<TextView>(R.id.itemCategorySignle)
        val itemDescription = view.findViewById<TextView>(R.id.itemDescription)
        val itemImage = view.findViewById<ImageView>(R.id.itemImageSingle)
        val add = view.findViewById<ImageView>(R.id.addingCart)
        val remove = view.findViewById<ImageView>(R.id.removingCart)
        val itemCount = view.findViewById<TextView>(R.id.itemQuantityText)
        val addToCartBtn = view.findViewById<TextView>(R.id.addToCartBtn)

        // Get user id
        val userID = getUserID(requireContext())

        // Fetch product by id
        val call = RetrofitInstance.api.getProductById(productId!!)
        call.enqueue(object : retrofit2.Callback<Product> {
            override fun onResponse(call: retrofit2.Call<Product>, response: retrofit2.Response<Product>) {
                if (response.isSuccessful) {
                    val products = response.body()
                    Log.i("CategoryFragment", "Response Body: $products")
                    products?.let {
                        itemTitle.text = it.productName
                        itemPrice.text = it.unitPrice.toString()
                        itemVendor.text = it.vendorName
                        itemCategory.text = it.categoryName
                        itemDescription.text = it.productDescription
                        Glide.with(requireContext())
                            .load(it.image)
                            .into(itemImage)
                        itemCount.text = "0"
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<Product>, t: Throwable) {
                println("Error: ${t.message}")
            }
        })

        // Handle add and remove item count
        add.setOnClickListener {
            var count = itemCount.text.toString().toInt()
            count++
            itemCount.text = count.toString()
        }

        remove.setOnClickListener {
            var count = itemCount.text.toString().toInt()
            if (count > 0) {
                count--
            }
            itemCount.text = count.toString()
        }

        // Handle add to cart
        addToCartBtn.setOnClickListener {
            // Add to cart

            if(itemCount.text.toString().toInt() == 0){
                val toast = Toast.makeText(requireContext(), "Please add item to cart", Toast.LENGTH_SHORT)
                toast.show()
                return@setOnClickListener
            }

            val cart = AddToCartRequest(
                productId = productId!!,
                quantity = itemCount.text.toString().toInt(),
                userId = userID!!
            )

            val call = RetrofitInstance.api.addProduct(cart)
            call.enqueue(object : retrofit2.Callback<OrderItem> {
                override fun onResponse(call: retrofit2.Call<OrderItem>, response: retrofit2.Response<OrderItem>) {
                    if (response.isSuccessful) {
                        val products = response.body()
                        Log.i("CategoryFragment", "Response Body: $products")

                        val toast = Toast.makeText(requireContext(), "Item Added to cart", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<OrderItem>, t: Throwable) {
                    println("Error: ${t.message}")
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