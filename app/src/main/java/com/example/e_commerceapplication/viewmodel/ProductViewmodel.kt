package com.example.e_commerceapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapplication.api.RetrofitInstance
import com.example.e_commerceapplication.models.Product
import com.example.e_commerceapplication.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewmodel @Inject constructor() : ViewModel() {

    private val _productList = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val productList: Flow<Resource<List<Product>>> = _productList

    private val _productListByCategory =
        MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val productListByCategory: Flow<Resource<List<Product>>> = _productListByCategory

    private val _searhedProduct = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val searchedProduct = _searhedProduct.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            _productList.emit(Resource.Loading())

            try {
                val list = RetrofitInstance.apiService.getProducts()
                _productList.emit(Resource.Success(list))
            } catch (e: Exception) {
                _productList.emit(Resource.Error(e.toString()))
            }
        }
    }

    fun getFilteredProductsByCategory(category: String) {
        viewModelScope.launch {
            _productListByCategory.emit(Resource.Loading())
        }

        try {
            val originalList = _productList.value.data?.toList()
            Log.d("ProductViewModel", originalList.toString())
            viewModelScope.launch {
                if (originalList != null) {
                    val filteredList = originalList.filter { items ->
                        items.category.toLowerCase().contains(category)
                    }
                    _productListByCategory.emit(Resource.Success(filteredList))
                } else {
                    _productListByCategory.emit(Resource.Error("Original list is null"))
                }
            }
        } catch (e: Exception) {
            viewModelScope.launch {
                _productListByCategory.emit(Resource.Error(e.message))
            }
        }

    }

    fun searchProduct(searhQuery: String) {
        viewModelScope.launch {
            _searhedProduct.emit(Resource.Loading())
        }

        try {
            val originalList = _productList.value.data?.toList()
            val filteredList = originalList?.filter { item ->
                item.name.toLowerCase().contains(searhQuery) ||
                        item.category.toLowerCase().contains(searhQuery)
            }
            Log.d("ProductViewModel", originalList.toString())
            viewModelScope.launch {
                _searhedProduct.emit(Resource.Success(filteredList))
            }
        } catch (e: Exception) {
            viewModelScope.launch {
                _searhedProduct.emit(Resource.Error(e.message))
            }
        }
    }
}