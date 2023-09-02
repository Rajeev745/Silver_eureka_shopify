package com.example.e_commerceapplication.fragments.shopping

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerceapplication.R
import com.example.e_commerceapplication.adapter.CartProductsAdapter
import com.example.e_commerceapplication.databinding.FragmentCartBinding
import com.example.e_commerceapplication.helper.CartProductQuantity
import com.example.e_commerceapplication.utils.Resource
import com.example.e_commerceapplication.viewmodel.CartViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/** Fragment for showing items to the cart */
@AndroidEntryPoint
class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    val viewmodel by viewModels<CartViewmodel>()
    private val cartProductsAdapter by lazy {
        CartProductsAdapter()
    }
    private val TAG = "CartFragment"
    var totalPrice = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        lifecycleScope.launch {
            viewmodel.cartProduct.collect() {
                when (it) {
                    is Resource.Success -> {
                        if (it.data?.size == 0) {
                            showEmptyCart()
                        } else {
                            hideEmptyCart()
                            cartProductsAdapter.differ.submitList(it.data)
                        }
                    }
                    is Resource.Error -> {
                        showEmptyCart()
                    }
                    is Resource.Loading -> {
                        Log.d(TAG, "Fetching cart list")
                    }
                    else -> Unit
                }
            }
        }

        // Directing to product details page on click of a product in cart
        cartProductsAdapter.onClick = {
            val bundle = Bundle().apply { putParcelable("product", it.product) }
            findNavController().navigate(
                R.id.action_cartFragment_to_product_detail_fragment,
                bundle
            )
        }

        cartProductsAdapter.onIncreaseClick = {
            viewmodel.changeQuantity(it, CartProductQuantity.INCREASE)
        }

        cartProductsAdapter.onDecreaseClick = {
            viewmodel.changeQuantity(it, CartProductQuantity.DECREASE)
        }

        cartProductsAdapter.onDeleteClick = {
            val alertDialog = AlertDialog.Builder(requireContext()).apply {
                setTitle("Delete Product from Cart")
                setMessage("Are you sure you want to delete this product")
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                setPositiveButton("Proceed") { dialog, _ ->
                    viewmodel.deleteProductFromCart(it)
                    dialog.dismiss()
                }
            }
            alertDialog.create()
            alertDialog.show()
        }

        lifecycleScope.launch {
            viewmodel.productPrice.collectLatest { price ->
                price?.let {
                    binding.totalAmoutPrice.text = price.toString()
                    totalPrice = price
                }
            }
        }

        lifecycleScope.launch {
            viewmodel.deleteDialog.collect() {
                val alertDialog = AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete Product from Cart")
                    setMessage("Are you sure you want to delete this product")
                    setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    setPositiveButton("Proceed") { dialog, _ ->
                        viewmodel.deleteProductFromCart(it)
                        dialog.dismiss()
                    }
                }
                alertDialog.create()
                alertDialog.show()
            }
        }

        // It will take you to the billing fragment
        binding.cartCheckoutBtn.setOnClickListener {
            val action = CartFragmentDirections.actionCartFragmentToBillingFragment(
                cartProductsAdapter.differ.currentList.toTypedArray(),
                totalPrice
            )
            findNavController().navigate(action)
        }
    }

    fun hideEmptyCart() {
        binding.apply {
            cartItemRecyclerView.visibility = View.VISIBLE
            cartText.visibility = View.VISIBLE
            cartCheckoutBtn.visibility = View.VISIBLE
            totalAmoutLyt.visibility = View.VISIBLE
            lottieAnimationView.visibility = View.GONE
            emptyCartText.visibility = View.GONE
        }
    }

    fun showEmptyCart() {
        binding.apply {
            cartItemRecyclerView.visibility = View.GONE
            cartText.visibility = View.GONE
            cartCheckoutBtn.visibility = View.GONE
            totalAmoutLyt.visibility = View.GONE
            lottieAnimationView.visibility = View.VISIBLE
            emptyCartText.visibility = View.VISIBLE
        }
    }

    private fun setUpRecyclerView() {
        binding.cartItemRecyclerView.apply {
            adapter = cartProductsAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

}