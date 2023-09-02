package com.example.e_commerceapplication.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapplication.ShoppingApplication
import com.example.e_commerceapplication.helper.Constants.USER_COLLECTION
import com.example.e_commerceapplication.models.User
import com.example.e_commerceapplication.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UserViewmodel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val storage: StorageReference,
    application: Application
) : AndroidViewModel(application) {

    private val _userInfo = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val userInfo = _userInfo.asStateFlow()

    private val _updateUserInfo = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val updateUserInfo = _updateUserInfo.asStateFlow()

    init {
        getUserInfoFromFirebase()
    }

    fun getUserInfoFromFirebase() {
        viewModelScope.launch {
            _userInfo.emit(Resource.Loading())
        }

        firestore.collection(USER_COLLECTION).document(firebaseAuth.uid!!).get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                user?.let {
                    viewModelScope.launch {
                        _userInfo.emit(Resource.Success(user))
                    }
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _userInfo.emit(Resource.Error(it.message))
                }
            }
    }

    fun editUserInformation(user: User, imageUri: Uri) {

        val validateInput = user.email.isNotEmpty() &&
                user.mobile.isNotEmpty() &&
                user.userName.isNotEmpty()

        if (!validateInput) {
            viewModelScope.launch {
                _updateUserInfo.emit(Resource.Error("Please check your inputs"))
            }
            return
        }

        viewModelScope.launch {
            viewModelScope.launch {
                _updateUserInfo.emit(Resource.Loading())
            }
        }

        if (imageUri == null) {
            saveUserInformation(user, true)
        } else {
            saveUserInformationWithNewImage(user, imageUri)
        }
    }

    private fun saveUserInformationWithNewImage(user: User, imageUri: Uri) {
        viewModelScope.launch {
            try {
                val imageBitmap = MediaStore.Images.Media.getBitmap(
                    getApplication<ShoppingApplication>().contentResolver,
                    imageUri
                )
                val byteArrayToOutputStream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 96, byteArrayToOutputStream)
                val imageByteArray = byteArrayToOutputStream.toByteArray()
                val imageDirectory =
                    storage.child("userImage/${firebaseAuth.uid}/${UUID.randomUUID()}")
                val result = imageDirectory.putBytes(imageByteArray).await()
                val imageUrl = result.storage.downloadUrl.await().toString()
                saveUserInformation(user.copy(userImage = imageUrl), false)
            } catch (e: Exception) {
                viewModelScope.launch {
                    _updateUserInfo.emit(Resource.Error(e.message))
                }
            }
        }
    }

    private fun saveUserInformation(user: User, b: Boolean) {
        firestore.runTransaction { transaction ->
            val docRef = firestore.collection(USER_COLLECTION).document(firebaseAuth.uid!!)
            if (b) {
                val currentUser = transaction.get(docRef).toObject(User::class.java)
                val newUser = currentUser?.copy(userImage = currentUser?.userImage ?: "")
                transaction.set(docRef, newUser!!)
            } else {
                transaction.set(docRef, user)
            }
        }.addOnSuccessListener {
            viewModelScope.launch {
                _updateUserInfo.emit(Resource.Success(user))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _updateUserInfo.emit(Resource.Error(it.message))
            }
        }
    }
}