package com.example.e_commerceapplication.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerceapplication.R
import com.example.e_commerceapplication.adapter.AddressRecyclerViewAdapter
import com.example.e_commerceapplication.adapter.BillingProductRecyclerViewAdapter
import com.example.e_commerceapplication.databinding.FragmentBillingBinding
import com.example.e_commerceapplication.models.Address
import com.example.e_commerceapplication.models.CartProduct
import com.example.e_commerceapplication.models.Order
import com.example.e_commerceapplication.utils.OrderStatus
import com.example.e_commerceapplication.utils.Resource
import com.example.e_commerceapplication.viewmodel.BillingViewmodel
import com.example.e_commerceapplication.viewmodel.OrdersViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Fragment for handling the billing logic */

// TODO: Address selection logic
// TODO: Payment through razor pay api

@AndroidEntryPoint
class BillingFragment : Fragment() {

    private lateinit var binding: FragmentBillingBinding
    private val addressRecyclerViewAdapter by lazy { AddressRecyclerViewAdapter() }
    private val billingProductRecyclerViewAdapter by lazy { BillingProductRecyclerViewAdapter() }
    private val billingViewmodel by viewModels<BillingViewmodel>()
    private val ordersViewmodel by viewModels<OrdersViewmodel>()
    val args by navArgs<BillingFragmentArgs>()
    var totalPrice = 0f
    var products = emptyList<CartProduct>()
    var selectedAddress: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // getting the products details to be checked out
        products = args.cartProducts.toList()
        totalPrice = args.totalPrice
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpProductRecyclerView()
        setUpAddressRecyclerView()

        lifecycleScope.launch {
            billingViewmodel.address.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        addressRecyclerViewAdapter.differ.submitList(it.data)
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {

                    }
                    else -> Unit
                }
            }
        }

        addressRecyclerViewAdapter.onClick = {
            selectedAddress = it
        }

        billingProductRecyclerViewAdapter.differ.submitList(products)
        binding.totalAmoutPrice.text = totalPrice.toString()

        binding.addAddressDrawable.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }

        /**
         * For purchasing the order
         */
        binding.billingProceedBtn.setOnClickListener {
            if (selectedAddress == null) {
                Toast.makeText(requireContext(), "Please select an address", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            setUpDialog()
            lifecycleScope.launch {
                ordersViewmodel.placeOrder.collect() {
                    when (it) {
                        is Resource.Error -> {
                            binding.billingProceedBtn.revertAnimation()
                            Toast.makeText(
                                requireContext(),
                                "Unable to place order please try again later",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        is Resource.Success -> {
                            binding.billingProceedBtn.revertAnimation()
                            Toast.makeText(
                                requireContext(),
                                "Placed order successfully",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        is Resource.Loading -> {
                            binding.billingProceedBtn.startAnimation()
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun setUpDialog() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Place order")
            setMessage("Are you sure you want to place order")
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Proceed") { dialog, _ ->
                val order =
                    Order(OrderStatus.Ordered.status, products, selectedAddress!!, totalPrice)
                ordersViewmodel.placeOrderForUser(order)
                dialog.dismiss()
            }
        }

        alertDialog.create()
        alertDialog.show()
    }

    private fun setUpAddressRecyclerView() {
        binding.recyclerViewBillingAddress.apply {
            adapter = addressRecyclerViewAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setUpProductRecyclerView() {
        binding.recyclerViewBillingItem.apply {
            adapter = billingProductRecyclerViewAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

}