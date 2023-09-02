package com.example.e_commerceapplication.fragments.usercredentials

import android.content.Intent
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
import com.example.e_commerceapplication.R
import com.example.e_commerceapplication.activities.ShoppingActivity
import com.example.e_commerceapplication.databinding.FragmentLoginBinding
import com.example.e_commerceapplication.dialog.setUpBottomSheetDialog
import com.example.e_commerceapplication.utils.Resource
import com.example.e_commerceapplication.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewmodel by viewModels<LoginViewModel>()
    private val TAG = "LoginFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogin.setOnClickListener {
            viewmodel.signingWithEmailAndPassword(
                binding.editTextEmail.text.toString(),
                binding.editTextPassword.text.toString()
            )
        }

        binding.registerText.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }

        binding.forgetPasswordText.setOnClickListener {
            setUpBottomSheetDialog {
                viewmodel.resetPassword(it)
            }
        }

        lifecycleScope.launch {
            viewmodel.resetPassword.collect() {
                when (it) {
                    is Resource.Success -> {
                        Log.d(TAG, it.data.toString())
                        Snackbar.make(requireView(), "Reset link was sent to your email", Snackbar.LENGTH_LONG).show()
                    }
                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                        Snackbar.make(requireView(), "Error: ${it.message}", Snackbar.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewmodel.login.collect() {
                when (it) {
                    is Resource.Success -> {
                        Log.d(TAG, it.data.toString())
                        binding.buttonLogin.revertAnimation()
                        Intent(requireActivity(), ShoppingActivity::class.java).apply {
                            this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(this)
                        }
                    }
                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                        binding.buttonLogin.revertAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resource.Loading -> {
                        binding.buttonLogin.startAnimation()
                    }
                    else -> Unit
                }
            }
        }
    }
}