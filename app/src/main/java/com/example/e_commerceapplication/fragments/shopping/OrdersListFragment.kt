package com.example.e_commerceapplication.fragments.shopping

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerceapplication.R
import com.example.e_commerceapplication.adapter.OrderListRecyclerViewAdapter
import com.example.e_commerceapplication.databinding.FragmentOrdersListBinding
import com.example.e_commerceapplication.utils.Resource
import com.example.e_commerceapplication.viewmodel.OrderListViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Fragment for showing the orders list
 */
@AndroidEntryPoint
class OrdersListFragment : Fragment(R.layout.fragment_orders_list) {

    private lateinit var binding: FragmentOrdersListBinding
    private val viewmodel by viewModels<OrderListViewmodel>()
    private val orderListRecyclerViewAdapter by lazy { OrderListRecyclerViewAdapter() }
    private val TAG = "OrdersListFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        lifecycleScope.launch {
            viewmodel.getOrderList.collect() {
                when (it) {
                    is Resource.Success -> {
                        binding.orderListProgressBar.visibility = View.GONE
                        binding.recyclerViewOrderList.visibility = View.VISIBLE
                        orderListRecyclerViewAdapter.differ.submitList(it.data)
                        Log.d(TAG, it.data.toString())
                    }
                    is Resource.Error -> {
                        binding.orderListProgressBar.visibility = View.GONE
                        binding.recyclerViewOrderList.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {
                        binding.orderListProgressBar.visibility = View.VISIBLE
                        binding.recyclerViewOrderList.visibility = View.GONE
                    }
                    else -> Unit
                }
            }
        }

        orderListRecyclerViewAdapter.onClick = {
            val action = OrdersListFragmentDirections.actionOrdersListFragmentToOrderDetailsFragment(it)
            findNavController().navigate(action)
        }

    }

    private fun setUpRecyclerView() {
        binding.recyclerViewOrderList.apply {
            adapter = orderListRecyclerViewAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

}