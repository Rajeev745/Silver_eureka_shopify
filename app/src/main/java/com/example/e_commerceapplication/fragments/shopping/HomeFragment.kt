package com.example.e_commerceapplication.fragments.shopping

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerceapplication.R
import com.example.e_commerceapplication.adapter.HomeViewPagerAdapter
import com.example.e_commerceapplication.databinding.FragmentHomeBinding
import com.example.e_commerceapplication.databinding.FragmentRegisterBinding
import com.example.e_commerceapplication.fragments.category.*
import com.example.e_commerceapplication.models.Product
import com.example.e_commerceapplication.utils.Resource
import com.example.e_commerceapplication.viewmodel.ProductViewmodel
import com.example.e_commerceapplication.viewmodel.RegisterViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val TAG = "HomeFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragment = arrayListOf(
            HomeMainFragment(),
            AccessoriesFragment(),
            WatchesFragment(),
            LaptopFragment(),
            MobileFragment(),
        )

        val viewPagerAdapter = HomeViewPagerAdapter(categoriesFragment, childFragmentManager, lifecycle)
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLyt, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = "Home"
                1 -> tab.text = "Accessories"
                2 -> tab.text = "Watches"
                3 -> tab.text = "Laptops"
                4 -> tab.text = "Mobiles"
            }
        }.attach()

        binding.searchIcon.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

}