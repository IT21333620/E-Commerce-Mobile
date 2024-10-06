package com.example.e_commercemobile.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
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
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_category, container, false)
        recyclerView = view.findViewById(R.id.productRecyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        searchView = view.findViewById(R.id.searchView)

        productList = ArrayList()
        adapter = ProductAdapter(productList)
        recyclerView.adapter = adapter

        // Fetch products from the API
        fetchProducts()

        // Handle item click
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

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.i("CategoryFragment", "Query: $newText")
                filterList(newText)
                return true
            }
        })

        return view
    }

    private fun fetchProducts() {
        val call = RetrofitInstance.api.getProducts()
        call.enqueue(object : retrofit2.Callback<List<Product>> {
            override fun onResponse(call: retrofit2.Call<List<Product>>, response: retrofit2.Response<List<Product>>) {
                if (response.isSuccessful) {
                    val filteredList = response.body()?.let { ArrayList<Product>(it) }
                    productList.addAll(response.body()!!)
                    if (filteredList != null) {
                        adapter.setFilteredList(filteredList)
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<List<Product>>, t: Throwable) {
                println("Error: ${t.message}")
            }
        })
    }

    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<Product>()
            for (product in productList) {
                if (product.productName.lowercase().contains(query.lowercase())) {
                    filteredList.add(product)
                }
            }

            if (filteredList.isEmpty()) {
                val toast = Toast.makeText(context, "No products found", Toast.LENGTH_SHORT)
                toast.show()
            }
            adapter.setFilteredList(filteredList)
        }
    }
}