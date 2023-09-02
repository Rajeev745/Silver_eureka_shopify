package com.example.e_commerceapplication.fragments.shopping

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerceapplication.adapter.AdapterAddToCartClickListener
import com.example.e_commerceapplication.adapter.CategoryProductRecyclerViewAdapter
import com.example.e_commerceapplication.databinding.FragmentSearchBinding
import com.example.e_commerceapplication.models.CartProduct
import com.example.e_commerceapplication.models.Product
import com.example.e_commerceapplication.utils.Resource
import com.example.e_commerceapplication.viewmodel.DetailViewModel
import com.example.e_commerceapplication.viewmodel.ProductViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), AdapterAddToCartClickListener {

    private lateinit var binding: FragmentSearchBinding
    private val viewmodel by viewModels<ProductViewmodel>()
    private val detailViewmodel by viewModels<DetailViewModel>()
    private val categoryProductRecyclerViewAdapter by lazy {
        CategoryProductRecyclerViewAdapter()
    }
    private val TAG = "SearchFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        getSearchQuery()

        lifecycleScope.launch {
            viewmodel.searchedProduct.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        binding.lottieAnimationView.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        binding.searchRecyclerView.visibility = View.VISIBLE
                        if (it.data?.isEmpty() == true) {
                            binding.lottieAnimationView.visibility = View.VISIBLE
                        }
                        categoryProductRecyclerViewAdapter.differ.submitList(it.data)
                    }
                    is Resource.Error -> {
                        binding.lottieAnimationView.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        binding.searchRecyclerView.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {
                        binding.lottieAnimationView.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                        binding.searchRecyclerView.visibility = View.GONE
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
                        Toast.makeText(requireContext(), "Added Successfully", Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), "Something Went wrong", Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resource.Loading -> {
                        Log.d(TAG, "Loading")
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun getSearchQuery() {
        binding.searchEdtTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Unit
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Unit
            }

            override fun afterTextChanged(p0: Editable?) {
                viewmodel.searchProduct(p0.toString())
            }

        })
    }

    private fun setUpRecyclerView() {
        binding.searchRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = categoryProductRecyclerViewAdapter
        }
        categoryProductRecyclerViewAdapter.setOnItemClickListener(this)
    }

    override fun addToCartItemClickListener(itemPosition: Int, item: Product) {
        val cartProduct = CartProduct(item, item.colors[0], 1)
        detailViewmodel.addUpdateProductInCart(cartProduct)
    }

}