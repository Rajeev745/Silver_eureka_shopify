package com.example.e_commerceapplication.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerceapplication.databinding.FragmentAddressBinding
import com.example.e_commerceapplication.models.Address
import com.example.e_commerceapplication.utils.Resource
import com.example.e_commerceapplication.viewmodel.AddressViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/** Fragment for adding address for the user*/
@AndroidEntryPoint
class AddressFragment : Fragment() {

    private lateinit var binding: FragmentAddressBinding
    val viewmodel by viewModels<AddressViewmodel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewmodel.addAddress.collect() {
                when (it) {
                    is Resource.Success -> {
                        binding.addAddressBtn.revertAnimation()
                        findNavController().navigateUp()
                    }
                    is Resource.Loading -> {
                        binding.addAddressBtn.startAnimation()
                    }
                    is Resource.Error -> {
                        binding.addAddressBtn.revertAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewmodel.error.collect() {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            addAddressBtn.setOnClickListener {
                val localArea = binding.editTxtAreaAddress.text.trim().toString()
                val mobile = binding.editTxtAreaMobileNumber.text.trim().toString()
                val street = binding.editTxtStreet.text.trim().toString()
                val house = binding.editTxtHouse.text.trim().toString()
                val city = binding.editTxtCity.text.trim().toString()
                val state = binding.editTxtState.text.trim().toString()
                val country = binding.editTxtCountry.text.trim().toString()
                val pincode = binding.editTxtPin.text.trim().toString()
                val address =
                    Address(localArea, mobile, street, house, city, state, country, pincode)
                viewmodel.createAddress(address)
            }
        }
    }

}