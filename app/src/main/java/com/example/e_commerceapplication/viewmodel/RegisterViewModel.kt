package com.example.e_commerceapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.e_commerceapplication.helper.Constants
import com.example.e_commerceapplication.helper.Constants.USER_COLLECTION
import com.example.e_commerceapplication.models.LoginAndRegisterFieldState
import com.example.e_commerceapplication.models.User
import com.example.e_commerceapplication.utils.LoginAndRegisterValidation
import com.example.e_commerceapplication.utils.Resource
import com.example.e_commerceapplication.utils.validateEmail
import com.example.e_commerceapplication.utils.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: Flow<Resource<User>> = _register

    private val _validation = Channel<LoginAndRegisterFieldState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountWithEmail(user: User, password: String) {

        if (checkValidation(user, password)) {
            runBlocking {
                _register.emit(Resource.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        saveUserToFireBase(it.uid, user)
                    }
                }
                .addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                }
        } else {
            val loginAndRegisterFieldState = LoginAndRegisterFieldState(
                validateEmail(user.email),
                validatePassword(password)
            )
            runBlocking {
                _validation.send(loginAndRegisterFieldState)
            }
        }
    }

    private fun saveUserToFireBase(uid: String, user: User) {
        firestore.collection(USER_COLLECTION)
            .document(uid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }
            .addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
    }

    private fun checkValidation(user: User, password: String): Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)

        return (emailValidation is LoginAndRegisterValidation.Success
                && passwordValidation is LoginAndRegisterValidation.Success)
    }
}