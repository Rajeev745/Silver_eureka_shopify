package com.example.e_commerceapplication.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.e_commerceapplication.R
import com.example.e_commerceapplication.activities.ShoppingActivity
import com.example.e_commerceapplication.adapter.ColorRecyclerviewAdapter
import com.example.e_commerceapplication.databinding.FragmentProductDetailBinding
import com.example.e_commerceapplication.models.CartProduct
import com.example.e_commerceapplication.utils.Resource
import com.example.e_commerceapplication.viewmodel.DetailViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Fragment for showing the product details
 */

// TODO: Implement the logic for buy now button

@AndroidEntryPoint
class product_detail_fragment : Fragment(R.layout.fragment_product_detail) {

    private lateinit var binding: FragmentProductDetailBinding
    val colorRecyclerViewAdapter by lazy {
        ColorRecyclerviewAdapter()
    }
    private val args by navArgs<product_detail_fragmentArgs>()
    val viewmodel by viewModels<DetailViewModel>()
    var selectedColor = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        val bottomNavigationView =
            (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product = args.product

        setUpColorRecyclerView()

        binding.apply {
            productDetailPrice.text = product.price.toString()
            productDetailCompanyName.text = product.company
            productDetailName.text = product.name
            productDetailDescription.text = product.description
            Glide.with(requireView()).load(product.image).into(productDetailImage)

            if (product.shipping) {
                productDetailFreeDelievery.visibility = View.VISIBLE
            } else {
                productDetailFreeDelievery.visibility = View.INVISIBLE
            }
        }

        colorRecyclerViewAdapter.differ.submitList(product.colors)

        colorRecyclerViewAdapter.onClick = {
            selectedColor = it
        }

        // Button for adding product to the cart
        binding.addToCartBtnProductDetailFragment.setOnClickListener {
            val cartProduct = CartProduct(product, selectedColor, 1)
            viewmodel.addUpdateProductInCart(cartProduct)
        }

        lifecycleScope.launch {
            viewmodel.addToCart.collect() {
                when(it) {
                    is Resource.Error -> {
                        binding.addToCartBtnProductDetailFragment.revertAnimation()
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG).show()
                    }
                    is Resource.Success -> {
                        binding.addToCartBtnProductDetailFragment.revertAnimation()
                        Toast.makeText(requireContext(), "Added Successfully", Toast.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {
                        binding.addToCartBtnProductDetailFragment.startAnimation()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setUpColorRecyclerView() {
        binding.productDetailColorRecyclerView.apply {
            adapter = colorRecyclerViewAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

}