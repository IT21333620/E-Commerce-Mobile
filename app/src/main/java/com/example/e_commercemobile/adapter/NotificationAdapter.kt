package com.example.e_commercemobile.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commercemobile.R
import com.example.e_commercemobile.api.RetrofitInstance
import com.example.e_commercemobile.data.model.Notification
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class NotificationAdapter( private val NotificationList: ArrayList<Notification>)
    : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    var onItemClick: ((Notification) -> Unit)? = null

    class NotificationViewHolder(itemNotification: View) : RecyclerView.ViewHolder(itemNotification) {
        val orderId: TextView = itemView.findViewById(R.id.notificationOrderID)
        val notificationDate: TextView = itemView.findViewById(R.id.notificationDate)
        val notificationTime: TextView = itemView.findViewById(R.id.notificationTime)
        val notificationMessage: TextView = itemView.findViewById(R.id.notificationMessage)
        val markAsRead: Button = itemView.findViewById(R.id.markAsRead)
        val readIndicator: ImageView = itemView.findViewById(R.id.readIndicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notifications, parent, false)
        return NotificationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return NotificationList.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = NotificationList[position]

        holder.orderId.text = notification.orderId
        holder.notificationDate.text = formatCreatedDate(notification.createdTime)
        holder.notificationMessage.text = notification.message
        holder.notificationTime.text = formatCreatedTime(notification.createdTime)
        holder.markAsRead.setOnClickListener {
            markNotificationAsRead(notification, holder)
        }
        if (notification.markRead) {
            holder.readIndicator.visibility = View.INVISIBLE
        } else {
            holder.readIndicator.visibility = View.VISIBLE
        }
    }

    fun formatCreatedTime(createdTime: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = inputFormat.parse(createdTime)
        return outputFormat.format(date)
    }

    fun formatCreatedDate(createdTime: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(createdTime)
        return outputFormat.format(date)
    }


    private fun markNotificationAsRead(notification: Notification, holder: NotificationViewHolder) {
        Log.i("NotificationAdapter", notification.id)
        val updatedNotification = notification.copy(markRead = true)
        val call = RetrofitInstance.cartApi.markNotificationAsRead(notification.id, updatedNotification)
        call.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.i("NotificationAdapter", response.code().toString())
                if (response.isSuccessful) {
                    holder.readIndicator.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("NotificationAdapter", "API call failed: ${t.message}")
            }
        })
    }

}