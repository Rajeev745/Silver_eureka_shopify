package com.example.e_commerceapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapplication.helper.Constants.USER_COLLECTION
import com.example.e_commerceapplication.models.Order
import com.example.e_commerceapplication.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderListViewmodel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    private val _getOrderList = MutableStateFlow<Resource<List<Order>>>(Resource.Unspecified())
    val getOrderList = _getOrderList.asStateFlow()

    init {
        getOrderListFromDatabase()
    }

    private fun getOrderListFromDatabase() {
        viewModelScope.launch {
            _getOrderList.emit(Resource.Loading())
        }

        firestore.collection(USER_COLLECTION).document(firebaseAuth.uid!!).collection("orders").get()
            .addOnSuccessListener {
                val orders = it.toObjects(Order::class.java)
                viewModelScope.launch {
                    _getOrderList.emit(Resource.Success(orders))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _getOrderList.emit(Resource.Error(it.message))
                }
            }
    }
}