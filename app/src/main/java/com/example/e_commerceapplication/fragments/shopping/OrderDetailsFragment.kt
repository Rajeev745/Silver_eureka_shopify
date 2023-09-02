package com.example.e_commerceapplication.fragments.shopping

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerceapplication.adapter.BillingProductRecyclerViewAdapter
import com.example.e_commerceapplication.databinding.FragmentOrderDetailsBinding
import com.example.e_commerceapplication.utils.OrderStatus
import com.example.e_commerceapplication.utils.getOrderStatus

/**
 * Fragment for showing the details of orders
 */
class OrderDetailsFragment : Fragment() {

    private lateinit var binding: FragmentOrderDetailsBinding
    private val billingProductRecyclerViewAdapter by lazy {
        BillingProductRecyclerViewAdapter()
    }
    private val args by navArgs<OrderDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val order = args.ordersDetail
        setUpRecyclerView()

        binding.apply {
            tvOrderId.text = "Order #${order.orderId}"

            // Order status in a linear progress bar
            stepView.setSteps(
                mutableListOf(
                    OrderStatus.Ordered.status,
                    OrderStatus.Confirmed.status,
                    OrderStatus.Shipped.status,
                    OrderStatus.Delievered.status
                )
            )

            val currentOrderStatus = when (getOrderStatus(order.orderStatus!!)) {
                is OrderStatus.Ordered -> 0
                is OrderStatus.Confirmed -> 1
                is OrderStatus.Shipped -> 2
                is OrderStatus.Delievered -> 3
                else -> 0
            }

            stepView.go(currentOrderStatus, true)
            if (currentOrderStatus == 3) {
                stepView.done(true)
            }

            tvFullName.text = order.address.pinCode
            tvPhoneNumber.text = order.address.mobileNumber
            tvAddress.text =
                "${order.address.houseNumbre}, ${order.address.street}, ${order.address.city}"
            tvTotalprice.text = "Rs ${order.totalPrice.toString()}"
        }

        billingProductRecyclerViewAdapter.differ.submitList(order.productList)
    }

    private fun setUpRecyclerView() {
        binding.recyclerViewProducts.apply {
            adapter = billingProductRecyclerViewAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }
}