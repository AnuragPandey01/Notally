package xyz.droidev.notally.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast

fun Context.showToast(msg : String){
    Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
}

fun Context.showKeyboard(editText: EditText){
    editText.requestFocus()
    (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
}

fun Context.hideKeyboard(editText: EditText){
    (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(editText.windowToken,0)
}