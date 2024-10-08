package com.example.e_commercemobile.adapter

import android.text.Selection
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commercemobile.R
import com.example.e_commercemobile.api.RetrofitInstance
import com.example.e_commercemobile.data.model.Cart
import com.example.e_commercemobile.data.model.OrderItem
import okhttp3.ResponseBody

class CartAdapter(private val CartList: ArrayList<Cart>, private val onSelectionChanged: () -> Unit)
    : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    var onItemClick: ((Cart) -> Unit)? = null

    class CartViewHolder(itemCart: View) : RecyclerView.ViewHolder(itemCart) {
        val cartTitle: TextView = itemView.findViewById(R.id.cartItemName)
        val cartPrice: TextView = itemView.findViewById(R.id.cartPriceText)
        val quantity: TextView = itemView.findViewById(R.id.cartQuantityText)
        val vender: TextView = itemView.findViewById(R.id.cartVenderText)
        val select: CheckBox = itemView.findViewById(R.id.checkBox)
        val remove: ImageView = itemView.findViewById(R.id.cartItemRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return CartList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart = CartList[position]

        holder.cartTitle.text = cart.productName
        holder.cartPrice.text = cart.price.toString()
        holder.quantity.text = cart.quantity.toString()
        holder.vender.text = cart.vendorName

        holder.select.isChecked = cart.isSelected

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(cart)
        }

        holder.remove.setOnClickListener {
            removeItem(position)
        }

        holder.select.setOnCheckedChangeListener { _, isChecked ->
            cart.isSelected = isChecked
            onSelectionChanged()
        }
    }

    // Remove item from the cart
    private fun removeItem(position: Int) {
        val call = RetrofitInstance.cartApi.deleteCartItem(CartList[position].id)
        call.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(
                call: retrofit2.Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    CartList.removeAt(position)
                    notifyItemRemoved(position)
                    onSelectionChanged()
                }
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                println("Error: ${t.message}")
            }
        })
    }

    // Calculate total price of selected items
    fun getTotalPrice(): Double {
        var totalPrice = 0.0
        for (cart in CartList) {
            if (cart.isSelected) {
                totalPrice += cart.price
            }
        }
        return totalPrice
    }

    // Get selected items
    fun getSelectedItems(): List<Cart> {
        return CartList.filter { it.isSelected }
    }
}