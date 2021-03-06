package com.example.debri.utils

import com.example.debri.utils.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.example.debri.utils.ApplicationClass.Companion.mSharedPreferences


fun saveJwt(jwtToken: String) {
    val editor = mSharedPreferences.edit()
    editor.putString(X_ACCESS_TOKEN, jwtToken)

    editor.apply()
}

fun getJwt(): String? = mSharedPreferences.getString(X_ACCESS_TOKEN, null)
