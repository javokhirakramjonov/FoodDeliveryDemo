package uz.gita.fooddeliverydemo.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import uz.gita.fooddeliverydemo.R

fun Fragment.snackMessage(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).apply {
        setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.primaryDark))
        setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))
    }.show()
}

fun Fragment.snackErrorMessage(message: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(requireView(), message, duration).apply {
        setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.error))
        setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))
    }.show()
}

fun Fragment.hideKeyboard() {
    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, 0)
}