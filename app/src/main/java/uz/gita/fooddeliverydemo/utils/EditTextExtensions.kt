package uz.gita.fooddeliverydemo.utils

import android.annotation.SuppressLint
import android.content.Context
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import androidx.appcompat.widget.AppCompatImageView
import uz.gita.fooddeliverydemo.R

@SuppressLint("UseCompatLoadingForDrawables")
fun EditText.setBackgroundByState(state: Int, context: Context) {
    when (state) {
        -1 -> {
            this.background = resources.getDrawable(
                R.drawable.error_edit_text,
                context.theme
            )
            this.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0)
        }
        0 -> {
            this.background = resources.getDrawable(
                R.drawable.active_edit_text,
                context.theme
            )
            this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }
        1 -> {
            this.background = resources.getDrawable(
                R.drawable.correct_edit_text,
                context.theme
            )
            this.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_correct, 0)
        }
    }
}

fun EditText.passwordVisible(state: Boolean, button: AppCompatImageView) {
    if (state) {
        button.setImageResource(R.drawable.ic_eye_hidden)
        this.transformationMethod = HideReturnsTransformationMethod()
    } else {
        button.setImageResource(R.drawable.ic_eye)
        this.transformationMethod = PasswordTransformationMethod()
    }
    this.setSelection(this.text.toString().length)
}