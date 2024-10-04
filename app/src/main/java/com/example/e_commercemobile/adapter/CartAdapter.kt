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
import com.example.e_commercemobile.data.model.Cart
import com.example.e_commercemobile.data.model.OrderItem

class CartAdapter(private val CartList: ArrayList<Cart>)
    : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

        var onItemClick : ((Cart) -> Unit)? = null

    class CartViewHolder (itemCart: View): RecyclerView.ViewHolder(itemCart){

        val cartTitle : TextView = itemView.findViewById(R.id.cartItemName)
        val cartPrice : TextView = itemView.findViewById(R.id.cartPriceText)
        val quantity : TextView = itemView.findViewById(R.id.cartQuantityText)
        val select : CheckBox = itemView.findViewById(R.id.checkBox)
        val remove : ImageView = itemView.findViewById(R.id.cartItemRemove)
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
        

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(cart)
        }
    }
}