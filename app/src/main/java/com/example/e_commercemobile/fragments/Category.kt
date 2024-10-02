package com.example.e_commercemobile.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commercemobile.R
import com.example.e_commercemobile.adapter.ProductAdapter
import com.example.e_commercemobile.api.RetrofitInstance
import com.example.e_commercemobile.data.model.Product

class category : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productList: ArrayList<Product>
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_category, container, false)
        recyclerView = view.findViewById(R.id.productRecyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        productList = ArrayList()
//        productList.add(Product("1", "Product 1", "Description 1", 100.00,20, "https://via.placeholder.com/150", "Food", "Ashen"))
//        productList.add(Product("2", "Product 2", "Description 2", 200.00,20, "https://via.placeholder.com/150", "Food", "Ashen"))


        val call = RetrofitInstance.api.getProducts()
        call.enqueue(object : retrofit2.Callback<List<Product>> {
            override fun onResponse(call: retrofit2.Call<List<Product>>, response: retrofit2.Response<List<Product>>) {
                if (response.isSuccessful) {
                    val products = response.body()
                    Log.i("CategoryFragment", "Response Body: $products")
                    if (products != null) {
                        productList.addAll(products)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<List<Product>>, t: Throwable) {
                println("Error: ${t.message}")
            }
        })

        adapter = ProductAdapter(productList)
        recyclerView.adapter = adapter

        adapter.onItemClick = {
            val fragment = SingleProductView()
            val bundle = Bundle()
            bundle.putString("product_id", it.id)
            fragment.arguments = bundle
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }


        return view
    }


}