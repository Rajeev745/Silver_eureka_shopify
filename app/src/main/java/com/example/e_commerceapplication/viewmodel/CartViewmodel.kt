package com.example.e_commerceapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapplication.helper.CartProductQuantity
import com.example.e_commerceapplication.helper.Constants.USER_COLLECTION
import com.example.e_commerceapplication.models.CartProduct
import com.example.e_commerceapplication.utils.FirebaseProductCart
import com.example.e_commerceapplication.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewmodel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseProductCart: FirebaseProductCart
) : ViewModel() {

    private val _cartProduct = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    val cartProduct = _cartProduct.asStateFlow()

    private val _deleteDialog = MutableSharedFlow<CartProduct>()
    val deleteDialog = _deleteDialog.asSharedFlow()

    var documentSnapshot = emptyList<DocumentSnapshot>()

    val productPrice = cartProduct.map {
        when (it) {
            is Resource.Success -> {
                it.data?.sumByDouble { product ->
                    (product.product.price * product.quantity).toDouble()
                }?.toFloat()
            }
            else -> null
        }
    }

    init {
        getCartProduct()
    }

    fun deleteProductFromCart(product: CartProduct) {
        val index = cartProduct.value.data?.indexOf(product)
        if (index != null && index != -1) {
            val documentRef = documentSnapshot[index].id
            firestore.collection(USER_COLLECTION).document(firebaseAuth.uid!!).collection("cart")
                .document(documentRef).delete()
        }
    }

    private fun getCartProduct() {
        viewModelScope.launch {
            _cartProduct.emit(Resource.Loading())
        }

        firestore.collection(USER_COLLECTION).document(firebaseAuth.uid!!).collection("cart")
            .addSnapshotListener { value, error ->
                if (value == null || error != null) {
                    viewModelScope.launch { _cartProduct.emit(Resource.Error(error?.message.toString())) }
                } else {
                    documentSnapshot = value.documents
                    val cartProductToObj = value.toObjects(CartProduct::class.java)
                    viewModelScope.launch {
                        _cartProduct.emit(Resource.Success(cartProductToObj))
                    }
                }
            }
    }

    fun changeQuantity(product: CartProduct, cartProductQuantity: CartProductQuantity) {
        val index = cartProduct.value.data?.indexOf(product)
        if (index != null && index != -1) {
            val documentId = documentSnapshot[index].id
            when (cartProductQuantity) {
                CartProductQuantity.INCREASE -> {
                    increaseQuantity(documentId)
                }
                CartProductQuantity.DECREASE -> {
                    if (product.quantity == 1) {
                        viewModelScope.launch {
                            _deleteDialog.emit(product)
                        }
                        return
                    }
                    decreaseQuantity(documentId)
                }
            }
        }
    }

    private fun increaseQuantity(documentId: String) {
        firebaseProductCart.updateProductInCart(documentId) { value, error ->
            if (error != null) {
                viewModelScope.launch {
                    _cartProduct.emit(Resource.Error(error.message))
                }
            }
        }
    }

    private fun decreaseQuantity(documentId: String) {
        firebaseProductCart.updateDecreaseProductInCart(documentId) { value, error ->
            if (error != null) {
                viewModelScope.launch {
                    _cartProduct.emit(Resource.Error(error.message))
                }
            }
        }
    }
}