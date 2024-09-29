package com.example.e_commercemobile

import android.content.Intent
import android.icu.util.ULocale.Category
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.e_commercemobile.fragments.cart
import com.example.e_commercemobile.fragments.category
import com.example.e_commercemobile.fragments.history
import com.example.e_commercemobile.fragments.profile

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imgCategory:ImageView = findViewById(R.id.imgCategory)
        val imgCart:ImageView = findViewById(R.id.imgCart)
        val imgHistory: ImageView = findViewById(R.id.imgHistory)
        val imgProfile: ImageView = findViewById(R.id.imgProfile)

        val fragmentCategory = category()
        val fragmentCart = cart()
        val fragmentHistory = history()
        val fragmentProfile = profile()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragmentCategory)
            commit()
        }
        imgCategory.setImageResource(R.drawable.baseline_category_48_selected)


        imgCategory.setOnClickListener{
            imgCategory.setImageResource(R.drawable.baseline_category_48_selected)
            imgCart.setImageResource(R.drawable.baseline_shopping_cart_48)
            imgHistory.setImageResource(R.drawable.baseline_history_toggle_off_48)
            imgProfile.setImageResource(R.drawable.baseline_person_48)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView, fragmentCategory)
                commit()
            }
        }

        imgCart.setOnClickListener{
            imgCategory.setImageResource(R.drawable.baseline_category_48)
            imgCart.setImageResource(R.drawable.baseline_shopping_cart_48_selected)
            imgHistory.setImageResource(R.drawable.baseline_history_toggle_off_48)
            imgProfile.setImageResource(R.drawable.baseline_person_48)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView, fragmentCart)
                commit()
            }
        }

        imgHistory.setOnClickListener{
            imgCategory.setImageResource(R.drawable.baseline_category_48)
            imgCart.setImageResource(R.drawable.baseline_shopping_cart_48)
            imgHistory.setImageResource(R.drawable.baseline_history_toggle_off_48_selected)
            imgProfile.setImageResource(R.drawable.baseline_person_48)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView, fragmentHistory)
                commit()
            }
        }

        imgProfile.setOnClickListener{
            imgCategory.setImageResource(R.drawable.baseline_category_48)
            imgCart.setImageResource(R.drawable.baseline_shopping_cart_48)
            imgHistory.setImageResource(R.drawable.baseline_history_toggle_off_48)
            imgProfile.setImageResource(R.drawable.baseline_person_48_selected)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView, fragmentProfile)
                commit()
            }
        }



    }
}