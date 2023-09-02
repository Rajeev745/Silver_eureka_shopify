package com.example.e_commerceapplication.fragments.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.e_commerceapplication.R
import com.example.e_commerceapplication.activities.ShoppingActivity
import com.example.e_commerceapplication.adapter.AdapterAddToCartClickListener
import com.example.e_commerceapplication.adapter.ProductsRecyclerViewAdapter
import com.example.e_commerceapplication.databinding.FragmentHomeMainBinding
import com.example.e_commerceapplication.models.CartProduct
import com.example.e_commerceapplication.models.Product
import com.example.e_commerceapplication.utils.Resource
import com.example.e_commerceapplication.viewmodel.DetailViewModel
import com.example.e_commerceapplication.viewmodel.ProductViewmodel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeMainFragment : Fragment(), AdapterAddToCartClickListener {

    private lateinit var binding: FragmentHomeMainBinding
    private lateinit var productsRecyclerViewAdapter: ProductsRecyclerViewAdapter
    private val viewmodel by viewModels<ProductViewmodel>()
    private val detailViewmodel by viewModels<DetailViewModel>()
    private val TAG = "HomeMainFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeMainBinding.inflate(inflater, container, false)
        val bottomNavigationView =
            (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        lifecycleScope.launch {
            viewmodel.productList.collect() {
                when (it) {
                    is Resource.Success -> {
                        Log.d(TAG, it.data.toString())
                        binding.lottieAnimationView.visibility = View.INVISIBLE
                        productsRecyclerViewAdapter.differ.submitList(it.data)
                    }
                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                    }
                    is Resource.Loading -> {
                        binding.lottieAnimationView.visibility = View.VISIBLE
                        Log.d(TAG, "Loading")
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            detailViewmodel.addToCart.collect() {
                when (it) {
                    is Resource.Success -> {
                        Log.d(TAG, it.data.toString())
                        Toast.makeText(requireContext(), "Added Successfully", Toast.LENGTH_LONG).show()
                    }
                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), "Something Went wrong", Toast.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {
                        Log.d(TAG, "Loading")
                    }
                    else -> Unit
                }
            }
        }

        productsRecyclerViewAdapter.onClick = {
            val bundle = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(
                R.id.action_homeFragment_to_product_detail_fragment,
                bundle
            )
        }
    }

    private fun setUpRecyclerView() {
        val screenWidth = resources.displayMetrics.widthPixels
        productsRecyclerViewAdapter = ProductsRecyclerViewAdapter(screenWidth)
        binding.bestProductsRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productsRecyclerViewAdapter
        }
        binding.bestInSellingText.visibility = View.GONE
        binding.bestInSellingRecyclerView.visibility = View.GONE
        productsRecyclerViewAdapter.setOnItemClickListener(this)
    }

    /**
     * Method for adding product to cart
     */
    override fun addToCartItemClickListener(itemPosition: Int, item: Product) {
        val cartProduct = CartProduct(item, item.colors[0], 1)
        detailViewmodel.addUpdateProductInCart(cartProduct)
    }
}