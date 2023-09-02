package com.example.e_commerceapplication.fragments.shopping

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.e_commerceapplication.R
import com.example.e_commerceapplication.activities.UserCredentialsActivity
import com.example.e_commerceapplication.databinding.FragmentUserProfileBinding
import com.example.e_commerceapplication.utils.Resource
import com.example.e_commerceapplication.viewmodel.UserProfileviewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {

    private lateinit var binding: FragmentUserProfileBinding
    val viewmodel by viewModels<UserProfileviewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            linearBilling.setOnClickListener {
                val action =
                    UserProfileFragmentDirections.actionUserProfileFragmentToBillingFragment(
                        emptyArray(),
                        0f
                    )
                findNavController().navigate(action)
            }

            linearOrders.setOnClickListener {
                findNavController().navigate(R.id.action_userProfileFragment_to_ordersListFragment)
            }

            constraintProfile.setOnClickListener {
                findNavController().navigate(R.id.action_userProfileFragment_to_userDetailFragment)
            }

            linearOut.setOnClickListener {
                viewmodel.logout()
                val intent = Intent(requireContext(), UserCredentialsActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }

        lifecycleScope.launch {
            viewmodel.user.collect() {
                when (it) {
                    is Resource.Error -> {
                        binding.progressbarSettings.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    is Resource.Success -> {
                        binding.progressbarSettings.visibility = View.GONE
                        Glide.with(this@UserProfileFragment).load(it.data?.userImage)
                            .error(R.drawable.ic_baseline_man_24).into(binding.imgUser)
                        binding.tvUserName.text = it.data?.userName
                    }
                    is Resource.Loading -> {
                        binding.progressbarSettings.visibility = View.VISIBLE
                    }
                    else -> Unit
                }
            }
        }
    }
}