package com.example.e_commerceapplication.fragments.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerceapplication.R
import com.example.e_commerceapplication.adapter.CategoryProductRecyclerViewAdapter
import com.example.e_commerceapplication.databinding.FragmentBaseBinding
import com.example.e_commerceapplication.utils.Resource
import com.example.e_commerceapplication.viewmodel.ProductViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
open class BaseFragment : Fragment(R.layout.fragment_base) {

    private lateinit var binding: FragmentBaseBinding
    private lateinit var categoryProductRecyclerView: CategoryProductRecyclerViewAdapter
    private val viewmodel by viewModels<ProductViewmodel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        lifecycleScope.launch {
            viewmodel.productListByCategory.collect() {
                when (it) {
                    is Resource.Success -> {
                        categoryProductRecyclerView.differ.submitList(it.data)
                        Log.d("BaseFragment", it.data.toString())
                    }
                    is Resource.Loading -> {
                        Log.d("BaseFragment", "Waiting for response")
                    }
                    is Resource.Error -> {
                        Log.e("BaseFragment", it.message.toString())
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        categoryProductRecyclerView = CategoryProductRecyclerViewAdapter()
        binding.categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryProductRecyclerView
        }
    }
}