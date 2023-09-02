package com.example.e_commerceapplication.fragments.usercredentials

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.e_commerceapplication.databinding.FragmentRegisterBinding
import com.example.e_commerceapplication.models.User
import com.example.e_commerceapplication.utils.LoginAndRegisterValidation
import com.example.e_commerceapplication.utils.Resource
import com.example.e_commerceapplication.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewmodel by viewModels<RegisterViewModel>()
    private val TAG = "RegisterFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonRegister.setOnClickListener {
                val user = User(
                    binding.editTextUsername.text.toString().trim(),
                    binding.editTextMobile.text.toString().trim(),
                    binding.editTextEmail.text.toString().trim(),
                    ""
                )
                val password = binding.editTextPassword.text.toString()

                viewmodel.createAccountWithEmail(user, password)
            }
        }

        lifecycleScope.launch {
            viewmodel.register.collect() {
                when (it) {
                    is Resource.Success -> {
                        Log.d(TAG, it.data.toString())
                        binding.buttonRegister.revertAnimation()
                    }
                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                        binding.buttonRegister.revertAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {
                        binding.buttonRegister.startAnimation()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewmodel.validation.collect() {
                if (it.email is LoginAndRegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.editTextEmail.apply {
                            requestFocus()
                            error = it.email.message
                        }
                    }
                }

                if (it.password is LoginAndRegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.editTextPassword.apply {
                            requestFocus()
                            error = it.password.message
                        }
                    }
                }
            }
        }
    }
}