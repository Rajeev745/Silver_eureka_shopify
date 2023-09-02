package com.example.e_commerceapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapplication.helper.Constants.USER_COLLECTION
import com.example.e_commerceapplication.models.Address
import com.example.e_commerceapplication.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewmodel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _address = MutableStateFlow<Resource<List<Address>>>(Resource.Unspecified())
    val address = _address.asStateFlow()

    init {
        fetchAddress()
    }

    private fun fetchAddress() {
        viewModelScope.launch { _address.emit(Resource.Loading()) }
        firestore.collection(USER_COLLECTION).document(firebaseAuth.uid!!).collection("address")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    viewModelScope.launch {
                        _address.emit(Resource.Error(error.message))
                    }
                    return@addSnapshotListener
                }
                val addresses = value?.toObjects(Address::class.java)
                viewModelScope.launch {
                    _address.emit(Resource.Success(addresses!!))
                }

            }
    }
}