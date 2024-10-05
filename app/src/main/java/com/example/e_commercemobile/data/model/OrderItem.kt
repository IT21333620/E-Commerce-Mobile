package com.example.e_commercemobile.data.model

data class OrderItem (
    val id : String,
    val productId : String,
    val orderId : String,
    val quantity : Int,
    val price : Double,
    val readyStatus: Boolean,
    val deliveredStatus: Boolean

    )