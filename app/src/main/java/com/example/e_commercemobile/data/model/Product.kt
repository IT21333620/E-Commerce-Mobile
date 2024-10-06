package com.example.e_commercemobile.data.model

data class Product(
    val id: String,
    val productName: String,
    val productDescription: String,
    val unitPrice: Double,
    val quantity: Int,
    val image: String,
    val rating: Double,
    val categoryName: String,
    val vendorName: String
)
