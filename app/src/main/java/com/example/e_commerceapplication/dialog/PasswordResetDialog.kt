package com.example.e_commerceapplication.dialog

import android.annotation.SuppressLint
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.e_commerceapplication.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * Dialog for asking the reset password option using the email id.
 * */
@SuppressLint("MissingInflatedId")
fun Fragment.setUpBottomSheetDialog(
    onSendClick: (String) -> Unit
) {
    val dialog = BottomSheetDialog(requireContext())
    val view = layoutInflater.inflate(R.layout.reset_password_dialog, null)
    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val emailText = view.findViewById<EditText>(R.id.reset_email_edttxt)
    val cancelBtn = view.findViewById<AppCompatButton>(R.id.cancel_btn)
    val resetBtn = view.findViewById<AppCompatButton>(R.id.reset_btn)

    resetBtn.setOnClickListener {
        val email = emailText.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }

    cancelBtn.setOnClickListener {
        dialog.dismiss()
    }
}