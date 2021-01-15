package com.arsvechkarev.vault.core.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun Context.showKeyboard() {
  val inputMethodManager =
      getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
  inputMethodManager!!.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

fun Context.hideKeyboard(editText: EditText) {
  val inputMethodManager =
      getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
  inputMethodManager!!.hideSoftInputFromWindow(editText.windowToken, 0)
}

val Context.screenWidth: Int
  get() = resources.displayMetrics.widthPixels

val Context.screenHeight: Int
  get() = resources.displayMetrics.heightPixels