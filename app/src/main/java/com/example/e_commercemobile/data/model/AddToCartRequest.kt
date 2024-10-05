package com.example.e_commercemobile.data.model

data class AddToCartRequest (
    val productId: String,
    val userId: String,
    val quantity: Int
)