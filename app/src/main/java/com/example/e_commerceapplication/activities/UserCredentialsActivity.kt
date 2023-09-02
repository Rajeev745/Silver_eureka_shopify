package com.example.e_commerceapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.e_commerceapplication.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserCredentialsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_credentials)
    }
}