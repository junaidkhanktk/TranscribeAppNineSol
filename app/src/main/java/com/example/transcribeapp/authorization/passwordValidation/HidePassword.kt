package com.example.transcribeapp.authorization.passwordValidation

import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText

fun EditText.hidePassword() {
    transformationMethod = object : PasswordTransformationMethod() {

        val HIDE_CHAR = '*'
        override fun getTransformation(source: CharSequence, view: View?): CharSequence {
            return object : CharSequence {
                override val length: Int
                    get() = source.length

                override fun get(index: Int): Char = HIDE_CHAR


                override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
                    return source.subSequence(startIndex, endIndex)
                }

            }
        }

    }
}


fun String.validatePassword(result: (List<String>) -> Unit) {
    val errorMsg = mutableListOf<String>()

    if (this.length < 8) {
        errorMsg.add("Password must be at least 8 characters long.")
    }
    if (!this.any { it.isUpperCase() }) {
        errorMsg.add("Password must contain at least 1 uppercase letter.")
    }
    if (!this.any { it.isLowerCase() }) {
        errorMsg.add("Password must contain at least 1 lowercase  letter.")

    }
    if (!this.any { it.isDigit() }) {
        errorMsg.add("Password must contain at least 1 number.")
    }

    result(errorMsg)
}


fun String.validateEmail(result: (String?) -> Unit) {
    val emailPattern = "[a-zA-z0-9]+@[a-z]+\\.+[a-z]+"

    if (this.matches(emailPattern.toRegex())) {
        result(null)
    } else {
        result("InValid email Address")
    }
}