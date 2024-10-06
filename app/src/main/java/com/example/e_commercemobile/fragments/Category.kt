package com.example.e_commercemobile.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Filter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commercemobile.R
import com.example.e_commercemobile.adapter.ProductAdapter
import com.example.e_commercemobile.api.RetrofitInstance
import com.example.e_commercemobile.data.model.Category
import com.example.e_commercemobile.data.model.Product

class category : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productList: ArrayList<Product>
    private lateinit var adapter: ProductAdapter
    private lateinit var searchView: SearchView
    private lateinit var autoComplete: AutoCompleteTextView
    private lateinit var clearFilter: TextView
    private lateinit var categories: List<Category>

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
        autoComplete = view.findViewById(R.id.autoCompleteTextView)
        clearFilter = view.findViewById(R.id.clearFilter)

        //fetch categories from the API
//        val languages = resources.getStringArray(R.array.languages)
//        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, languages)
//        autoComplete.setAdapter(arrayAdapter)


        productList = ArrayList()
        adapter = ProductAdapter(productList)
        recyclerView.adapter = adapter

        // Fetch products from the API
        fetchProducts()
        fetchCategoryList()

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

        // Clear dropdown selection on button click
        clearFilter.setOnClickListener {
            autoComplete.setText("Select Category", false)
            fetchProducts()
        }

        // Set item click listener for AutoCompleteTextView
        autoComplete.setOnItemClickListener { parent, _, position, _ ->
            val selectedCategory = parent.getItemAtPosition(position) as String
            val categoryId = categories.find { it.categoryName == selectedCategory }?.id
            if (categoryId != null) {
                fetchProductsByCategory(categoryId)
            }
        }

        return view
    }

    private fun fetchProducts() {
        val call = RetrofitInstance.api.getProductFilters("","")
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

    private fun fetchCategoryList () {
        val call = RetrofitInstance.api.getCategories()
        call.enqueue(object : retrofit2.Callback<List<Category>> {
            override fun onResponse(
                call: retrofit2.Call<List<Category>>,
                response: retrofit2.Response<List<Category>>
            ) {
                if (response.isSuccessful) {
                    categories = response.body() ?: emptyList()
                    val categoryNames = categories.map { it.categoryName }
                    val adapter =
                        ArrayAdapter(requireContext(), R.layout.dropdown_item, categoryNames)
                    autoComplete.setAdapter(adapter)
                } else {
                    Log.e("CategoryFragment", "Failed to fetch categories: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<Category>>, t: Throwable) {
                Log.e("CategoryFragment", "Error: ${t.message}")
            }
        })
    }

    private fun fetchProductsByCategory(categoryId: String) {
        val call = RetrofitInstance.api.getProductFilters("", categoryId)
        call.enqueue(object : retrofit2.Callback<List<Product>> {
            override fun onResponse(call: retrofit2.Call<List<Product>>, response: retrofit2.Response<List<Product>>) {
                if (response.isSuccessful) {
                    val filteredList = response.body()?.let { ArrayList<Product>(it) }
                    productList.clear()
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

}