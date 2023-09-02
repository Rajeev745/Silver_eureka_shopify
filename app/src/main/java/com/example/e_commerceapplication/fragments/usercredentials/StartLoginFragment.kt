package com.example.e_commerceapplication.fragments.usercredentials

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.e_commerceapplication.R
import com.example.e_commerceapplication.databinding.FragmentStartLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartLoginFragment:Fragment(R.layout.fragment_start_login) {

    private lateinit var binding: FragmentStartLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBtn.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        binding.registerBtn.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
    }
}