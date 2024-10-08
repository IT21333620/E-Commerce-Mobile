package com.example.e_commercemobile.data.model

data class Notification (
    val id: String,
    val createdTime: String,
    val orderId: String,
    val userId: String,
    val message: String,
    val markRead: Boolean
)