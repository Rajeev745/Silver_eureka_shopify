package com.example.e_commerceapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapplication.models.CartProduct
import com.example.e_commerceapplication.utils.FirebaseProductCart
import com.example.e_commerceapplication.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseProductCart: FirebaseProductCart
) : ViewModel() {

    private val _addToCart = MutableStateFlow<Resource<CartProduct>>(Resource.Unspecified())
    val addToCart = _addToCart.asStateFlow()

    fun addUpdateProductInCart(cartProduct: CartProduct) {
        viewModelScope.launch {
            _addToCart.emit(Resource.Loading())
        }

        firestore.collection("user_collection").document(firebaseAuth.uid!!).collection("cart")
            .whereEqualTo("product.id", cartProduct.product.id).get()
            .addOnSuccessListener {
                it.documents.let {
                    if (it.isEmpty()) {
                        // Adding a new product
                        addNewProduct(cartProduct)
                    } else {
                        // Updating the already existed product
                        val product = it.first().toObject(CartProduct::class.java)
                        if (product == cartProduct) {
                            // Increasing the quantity
                            val id = it.first().id
                            updateProduct(id, cartProduct)
                        } else {
                            // adding a new product
                            addNewProduct(cartProduct)
                        }
                    }
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _addToCart.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun addNewProduct(cartProduct: CartProduct) {
        firebaseProductCart.addProductToCart(cartProduct) { addedProduct, exception ->
            viewModelScope.launch {
                if (exception == null) {
                    _addToCart.emit(Resource.Success(addedProduct))
                } else {
                    _addToCart.emit(Resource.Error(exception.message))
                }
            }
        }
    }

    private fun updateProduct(documentId: String, cartProduct: CartProduct) {
        firebaseProductCart.updateProductInCart(documentId) { addedId, exception ->
            viewModelScope.launch {
                if (exception == null) {
                    _addToCart.emit(Resource.Success(cartProduct))
                } else {
                    _addToCart.emit(Resource.Error(exception.message))
                }
            }
        }
    }
}