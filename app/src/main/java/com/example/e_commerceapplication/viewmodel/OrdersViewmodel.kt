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
class OrdersViewmodel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _placeOrder = MutableStateFlow<Resource<Order>>(Resource.Unspecified())
    val placeOrder = _placeOrder.asStateFlow()

    fun placeOrderForUser(order: Order) {
        viewModelScope.launch { _placeOrder.emit(Resource.Loading()) }

        firestore.runBatch { batch ->
            firestore.collection(USER_COLLECTION).document(firebaseAuth.uid!!).collection("orders")
                .document().set(order)
            firestore.collection("orders").document().set(order)

            firestore.collection(USER_COLLECTION).document(firebaseAuth.uid!!).collection("cart")
                .get()
                .addOnSuccessListener {
                    it.forEach {
                        it.reference.delete()
                    }
                }
        }.addOnSuccessListener {
            viewModelScope.launch {
                _placeOrder.emit(Resource.Success(order))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _placeOrder.emit(Resource.Error(it.message))
            }
        }
    }
}