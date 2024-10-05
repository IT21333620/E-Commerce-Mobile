package com.example.e_commercemobile.data.model

import android.os.Parcel
import android.os.Parcelable

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
    val deliveredStatus: Boolean,
    var isSelected: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(productId)
        parcel.writeString(userId)
        parcel.writeString(productName)
        parcel.writeString(vendorId)
        parcel.writeString(vendorName)
        parcel.writeString(orderId)
        parcel.writeInt(quantity)
        parcel.writeDouble(price)
        parcel.writeByte(if (readyStatus) 1 else 0)
        parcel.writeByte(if (deliveredStatus) 1 else 0)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cart> {
        override fun createFromParcel(parcel: Parcel): Cart {
            return Cart(parcel)
        }

        override fun newArray(size: Int): Array<Cart?> {
            return arrayOfNulls(size)
        }
    }

}
