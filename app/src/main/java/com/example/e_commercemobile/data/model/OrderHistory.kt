package com.example.e_commercemobile.data.model

data class OrderHistory(
    val id: String,
    val orderDate: String,
    val orderStatus: String,
    val totalAmount: Double,
    val cancelStatus: Boolean,
    val editableStatus: Boolean,
    var customerId: String,
    var orderItems: List<Cart>
)

