package com.example.e_commercemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commercemobile.R
import com.example.e_commercemobile.api.RetrofitInstance
import com.example.e_commercemobile.data.model.OrderHistory
import okhttp3.Response
import okhttp3.ResponseBody


class OrderAdapter(private val OrderList: ArrayList<OrderHistory>)
    : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    var onItemClick: ((OrderViewHolder) -> Unit)? = null

    class OrderViewHolder(itemOrder: View) : RecyclerView.ViewHolder(itemOrder) {
        val orderID: TextView = itemView.findViewById(R.id.OrderIDtxt)
        val orderDate: TextView = itemView.findViewById(R.id.orderDateTxt)
        val orderTotal: TextView = itemView.findViewById(R.id.totalOrderTxt)
        val status: TextView = itemView.findViewById(R.id.orderStatusTxt)
        val cancelBtn: Button = itemView.findViewById(R.id.CancelOrder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return OrderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return OrderList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = OrderList[position]

        holder.orderID.text = order.id
        holder.orderTotal.text = order.totalAmount.toString()
        holder.orderDate.text = order.orderDate.split("T")[0]
        holder.status.text = order.orderStatus.toString()

        // Show or hide the cancel button based on editableStatus
        if (order.editableStatus) {
            holder.cancelBtn.visibility = View.VISIBLE
        } else {
            holder.cancelBtn.visibility = View.GONE
        }

        holder.cancelBtn.setOnClickListener {
            cancelOrder(order.id, holder)
        }

    }

    // Cancel order
    private fun cancelOrder(orderId: String, holder: OrderViewHolder) {
        val call = RetrofitInstance.cartApi.cancelOrder(orderId)
        call.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: retrofit2.Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(holder.itemView.context, "Order requested to cancelled", Toast.LENGTH_SHORT).show()
                    removeItem(holder.adapterPosition)
                } else {
                    Toast.makeText(holder.itemView.context, "Failed to request cancel order", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                Toast.makeText(holder.itemView.context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun removeItem(position: Int) {
        OrderList.removeAt(position)
        notifyItemRemoved(position)
    }
}