package com.example.e_commerceapplication.models

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroductionViewmodel @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val sharedPreferences: SharedPreferences
): ViewModel() {

    private val _navigate = MutableStateFlow(0)
    val navigate: StateFlow<Int> = _navigate

    init {
        val user = firebaseAuth.currentUser
        val isButtonClicked = sharedPreferences.getBoolean(INTRODUCTION_KEY, false)

        if (user != null) {
            viewModelScope.launch {
                _navigate.emit(SHOPPING_ACTIVITY)
            }
        } else if (isButtonClicked) {
            viewModelScope.launch {
                _navigate.emit(ACCOUNT_OPTION_FRAGMENT)
            }
        } else {
            Unit
        }
    }

    fun startButtonClicked() {
        sharedPreferences.edit().putBoolean(INTRODUCTION_KEY, true).apply()
    }

    companion object {
        private const val INTRODUCTION_KEY = "introduction_key"
        private const val SHOPPING_ACTIVITY = 100
        private const val ACCOUNT_OPTION_FRAGMENT = 200
    }
}