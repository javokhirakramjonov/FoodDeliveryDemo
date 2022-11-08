package uz.gita.fooddeliverydemo.presentation.viewModel

import androidx.lifecycle.LiveData

interface ResetPasswordScreenVM {
    val goMainScreenLiveData: LiveData<Unit>
    val isPasswordsOkLiveData: LiveData<Boolean>
    val loadingLiveData: LiveData<Boolean>
    val errorMessageLiveData: LiveData<String>
    val goBackLiveData: LiveData<Unit>

    fun checkPasswords(password1: String, password2: String)
    fun resetPassword(phone: String, password: String)
    fun goBack()
}