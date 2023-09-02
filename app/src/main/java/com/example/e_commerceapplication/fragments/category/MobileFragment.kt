package com.example.e_commerceapplication.fragments.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.e_commerceapplication.viewmodel.ProductViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MobileFragment: BaseFragment() {

    private val viewmodel by viewModels<ProductViewmodel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.getFilteredProductsByCategory("mobile")
    }
}