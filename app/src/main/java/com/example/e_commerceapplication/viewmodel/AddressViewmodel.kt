package com.example.e_commerceapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapplication.helper.Constants.USER_COLLECTION
import com.example.e_commerceapplication.models.Address
import com.example.e_commerceapplication.utils.Resource
import com.example.e_commerceapplication.utils.validateAddress
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewmodel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _addAddress = MutableStateFlow<Resource<Address>>(Resource.Unspecified())
    val addAddress = _addAddress.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    fun createAddress(address: Address) {
        val validation = validateAddress(address)

        if (validation) {
            viewModelScope.launch {
                _addAddress.emit(Resource.Loading())
            }
            firestore.collection(USER_COLLECTION).document(firebaseAuth.uid!!).collection("address")
                .add(address).addOnSuccessListener {
                    viewModelScope.launch {
                        _addAddress.emit(Resource.Success(address))
                    }
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _addAddress.emit(Resource.Error(it.message))
                    }
                }
        } else {
            viewModelScope.launch {
                _addAddress.emit(Resource.Error("All input fields must be filled"))
            }
        }
    }

    fun handleInputError() {

    }
}