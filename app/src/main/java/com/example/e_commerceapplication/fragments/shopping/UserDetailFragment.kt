package com.example.e_commerceapplication.fragments.shopping

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.e_commerceapplication.R
import com.example.e_commerceapplication.databinding.FragmentUserBinding
import com.example.e_commerceapplication.models.User
import com.example.e_commerceapplication.utils.Resource
import com.example.e_commerceapplication.viewmodel.UserViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserDetailFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding
    private val viewmodel by viewModels<UserViewmodel>()
    private var imageUri: Uri? = null
    private lateinit var imageActivityLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        imageActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                imageUri = it.data?.data
                Glide.with(this@UserDetailFragment).load(imageUri).into(binding.userProfileImage)
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewmodel.userInfo.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        binding.progreeBar.visibility = View.GONE
                        binding.userProfileLyt.visibility = View.VISIBLE
                        showUserContent(it.data)
                    }
                    is Resource.Error -> {
                        binding.progreeBar.visibility = View.GONE
                        binding.userProfileLyt.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {
                        binding.progreeBar.visibility = View.VISIBLE
                        binding.userProfileLyt.visibility = View.GONE
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewmodel.updateUserInfo.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        binding.buttonProfileSave.revertAnimation()
                        findNavController().navigateUp()
                    }
                    is Resource.Error -> {
                        binding.buttonProfileSave.revertAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {
                        binding.buttonProfileSave.startAnimation()
                    }
                    else -> Unit
                }
            }
        }

        binding.buttonProfileSave.setOnClickListener {
            binding.apply {
                val emailText = editProfileTextEmail.text.trim().toString()
                val mobileText = editProfileTextMobile.text.trim().toString()
                val userName = editProfileTextUsername.text.trim().toString()
                val user = User(userName, mobileText, emailText)
                viewmodel.editUserInformation(user, imageUri = imageUri!!)
            }
        }

        binding.userProfileImageEditBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            imageActivityLauncher.launch(intent)
        }

    }

    private fun showUserContent(data: User?) {
        binding.apply {
            Glide.with(this@UserDetailFragment).load(data?.userImage)
                .error(R.drawable.ic_baseline_man_24).into(userProfileImage)
            editProfileTextUsername.setText(data?.userName)
            editProfileTextMobile.setText(data?.mobile)
            editProfileTextEmail.setText(data?.email)
        }
    }
}