package xyz.droidev.notally.util

import android.text.TextUtils

fun String.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    return !TextUtils.isEmpty(this) && this.length >= 6
}