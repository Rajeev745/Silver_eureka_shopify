package com.example.e_commerceapplication.fragments.usercredentials

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerceapplication.R
import com.example.e_commerceapplication.activities.ShoppingActivity
import com.example.e_commerceapplication.databinding.FragmentIntroductionBinding
import com.example.e_commerceapplication.models.IntroductionViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntroductionFragment: Fragment(R.layout.fragment_introduction) {

    private lateinit var binding: FragmentIntroductionBinding
    val viewmodel by viewModels<IntroductionViewmodel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroductionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewmodel.navigate.collect() {
                when(it) {
                    100 -> {
                        Intent(requireActivity(), ShoppingActivity::class.java).apply {
                            this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(this)
                        }
                    }
                    200 -> {
                        findNavController().navigate(R.id.startLoginFragment)
                    }
                }
            }
        }
        binding.startBtn.setOnClickListener {
            viewmodel.startButtonClicked()
            findNavController().navigate(R.id.startLoginFragment)
        }
    }

}