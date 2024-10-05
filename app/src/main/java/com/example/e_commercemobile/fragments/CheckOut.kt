package com.example.e_commercemobile.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.e_commercemobile.R
import com.example.e_commercemobile.api.RetrofitInstance
import com.example.e_commercemobile.data.model.Cart
import com.example.e_commercemobile.data.model.OrderRequest
import okhttp3.ResponseBody
import kotlin.math.log

class CheckOut : Fragment() {

    private lateinit var selectedItems: List<Cart>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            selectedItems = it.getParcelableArrayList("selectedItems") ?: emptyList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_check_out, container, false)

        val totalPrice: TextView = view.findViewById(R.id.totalAmount)
        val cardNumber: EditText = view.findViewById(R.id.cardNum)
        val cardExpiry: EditText = view.findViewById(R.id.expireDate)
        val cvv: EditText = view.findViewById(R.id.cvv)
        val checkOutBtn: Button = view.findViewById(R.id.checkOutbtn)

        // Calculate the total price of the selected items
        calculateTotalPrice(totalPrice)

        // Handle the checkout button click
        checkOutBtn.setOnClickListener {
            val cardNumberText = cardNumber.text.toString()
            val cardExpiryText = cardExpiry.text.toString()
            val cvvText = cvv.text.toString()

            if (validateCardNumber(cardNumberText) && validateCardExpiry(cardExpiryText) && validateCVV(cvvText)) {
                // Proceed with the payment
                callPaymentApi()
            } else {
                // Show an error message
                Toast.makeText(requireContext(), "Invalid card details", Toast.LENGTH_SHORT).show()
            }

        }

        return view
    }


    // Calculate the total price of the selected items
    private fun calculateTotalPrice(totalPriceTextView: TextView) {
        var totalPrice = 0.0
        for (item in selectedItems) {
            totalPrice += item.price * item.quantity
        }
        totalPriceTextView.text = "$$totalPrice"
    }

    //Validate card number
    private fun validateCardNumber(cardNumber: String): Boolean {
        return cardNumber.length == 16 && cardNumber.all { it.isDigit() }
    }

    //Validate card expiry date
    private fun validateCardExpiry(cardExpiry: String): Boolean {
        val regex = Regex("^(0[1-9]|1[0-2])/([0-9]{2})\$")
        return regex.matches(cardExpiry)
    }

    //Validate CVV
    private fun validateCVV(cvv: String): Boolean {
        return cvv.length == 3 && cvv.all { it.isDigit() }
    }

    // Call payment API
    private fun callPaymentApi() {
        // Get user id
        val userID = getUserID(requireContext())

        val orderItemIDs = selectedItems.map { it.id}

        val orderRequest = OrderRequest(
            customerId = userID ?: "",
            orderItemIds = orderItemIDs
        )

        Log.i("CheckOutFragment", "Order Request: $orderRequest")

        val call = RetrofitInstance.cartApi.addCartItem(orderRequest)
        call.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: retrofit2.Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                Log.i("CheckOutFragment", "Response Body: ${response.code()}")
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Payment successful", Toast.LENGTH_SHORT).show()

                    // Navigate back to Cart fragment
                    val fragmentManager = activity?.supportFragmentManager
                    val fragmentTransaction = fragmentManager?.beginTransaction()
                    fragmentTransaction?.replace(R.id.fragmentContainerView, history())
                    fragmentTransaction?.addToBackStack(null)
                    fragmentTransaction?.commit()

                } else {
                    Toast.makeText(requireContext(), "Payment failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Get user id from shared preferences
    fun getUserID(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        return sharedPreferences.getString("USER_ID", null)
    }


}