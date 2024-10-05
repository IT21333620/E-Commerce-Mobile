package com.example.e_commercemobile.data.model

data class OrderRequest(
    val customerId: String,
    val orderItemIds: List<String>,
)
