package com.example.e_commerceapplication.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.e_commerceapplication.R
import com.example.e_commerceapplication.databinding.ActivityShoppingBinding
import com.example.e_commerceapplication.utils.Resource
import com.example.e_commerceapplication.viewmodel.CartViewmodel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityShoppingBinding.inflate(layoutInflater)
    }
    val viewmodel by viewModels<CartViewmodel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.shopping_fragment)
        binding.bottomNavigationView.setupWithNavController(navController)

        createBadgeForCart()
    }

    /**
     * Method for creating the badge on cart for showing the number of products
     * */
    fun createBadgeForCart() {
        lifecycleScope.launch {
            viewmodel.cartProduct.collect() {
                when (it) {
                    is Resource.Success -> {
                        val count = it.data?.size ?: 0
                        val bottomNavigationView =
                            findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
                        bottomNavigationView.getOrCreateBadge(R.id.cartFragment).apply {
                            number = count
                            backgroundColor = resources.getColor(R.color.g_red)
                            badgeTextColor = resources.getColor(R.color.white)
                        }
                    }
                    else -> Unit
                }
            }
        }
    }
}