package com.example.e_commercemobile.adapter

import android.media.Rating
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commercemobile.R
import com.example.e_commercemobile.data.model.Product

class ProductAdapter(private var ProductList: ArrayList<Product>)
    : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

        var onItemClick : ((Product) -> Unit)? = null

    class ProductViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val productImg: ImageView = itemView.findViewById(R.id.ProductImg)
        val productTitle : TextView = itemView.findViewById(R.id.ProductTitle)
        val productPrice : TextView = itemView.findViewById(R.id.ProductPrice)
        val productVendor : TextView = itemView.findViewById(R.id.ProductVendor)
        val productCategory : TextView = itemView.findViewById(R.id.ProductCategory)
        val rating : RatingBar = itemView.findViewById(R.id.ratingBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ProductList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = ProductList[position]
        Glide.with(holder.itemView.context)
            .load(product.image)
            .into(holder.productImg)
        holder.productTitle.text = product.productName
        holder.productPrice.text = product.unitPrice.toString()
        holder.productVendor.text = product.vendorName
        holder.productCategory.text = product.categoryName
        holder.rating.rating = product.rating.toFloat()

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(product)
        }
    }

    fun setFilteredList(filteredList: ArrayList<Product>){
        this.ProductList = filteredList
        notifyDataSetChanged()
    }
}