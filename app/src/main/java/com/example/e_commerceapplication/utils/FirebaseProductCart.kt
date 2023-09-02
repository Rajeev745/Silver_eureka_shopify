package com.example.e_commerceapplication.utils

import com.example.e_commerceapplication.helper.Constants.USER_COLLECTION
import com.example.e_commerceapplication.models.CartProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseProductCart(
    val firebaseAuth: FirebaseAuth,
    val firestore: FirebaseFirestore
) {

    val firebaseCollection =
        firestore.collection(USER_COLLECTION).document(firebaseAuth.uid!!).collection("cart")

    fun addProductToCart(cartProduct: CartProduct, onResult: (CartProduct?, Exception?) -> Unit) {
        firebaseCollection.document().set(cartProduct).addOnSuccessListener {
            onResult(cartProduct, null)
        }.addOnFailureListener {
            onResult(null, it)
        }
    }

    fun updateProductInCart(documentId: String, onResult: (String?, Exception?) -> Unit) {
        firestore.runTransaction { transaction ->
            val documentRef = firebaseCollection.document(documentId)
            val document = transaction.get(documentRef)
            val productObj = document.toObject(CartProduct::class.java)
            productObj?.let { cartProduct ->
                val updatedQuantity = cartProduct.quantity + 1
                val newProductObj = cartProduct.copy(quantity = updatedQuantity)
                transaction.set(documentRef, newProductObj)
            }
        }.addOnSuccessListener {
            onResult(documentId, null)
        }.addOnFailureListener {
            onResult(null, it)
        }
    }

    fun updateDecreaseProductInCart(documentId: String, onResult: (String?, Exception?) -> Unit) {
        firestore.runTransaction { transaction ->
            val documentRef = firebaseCollection.document(documentId)
            val document = transaction.get(documentRef)
            val productObj = document.toObject(CartProduct::class.java)
            productObj?.let { cartProduct ->
                val updatedQuantity = cartProduct.quantity - 1
                val newProductObj = cartProduct.copy(quantity = updatedQuantity)
                transaction.set(documentRef, newProductObj)
            }
        }.addOnSuccessListener {
            onResult(documentId, null)
        }.addOnFailureListener {
            onResult(null, it)
        }
    }
}