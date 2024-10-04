package com.example.e_commercemobile.data.model

data class Cart(
    val id: String,
    val productId: String,
    val userId: String,
    val productName: String,
    val vendorId: String,
    val vendorName: String,
    val orderId: String,
    val quantity: Int,
    val price: Double,
    val readyStatus: Boolean,
    val deliveredStatus: Boolean
)
