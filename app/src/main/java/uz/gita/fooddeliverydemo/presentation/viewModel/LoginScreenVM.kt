package uz.gita.fooddeliverydemo.presentation.viewModel

import androidx.lifecycle.LiveData

interface LoginScreenVM {
    val goMainScreenLiveData: LiveData<Unit>
    val goRegisterScreenLiveData: LiveData<Unit>
    val goForgotPasswordScreenLiveData: LiveData<Unit>
    val goGetStartedScreenLiveData: LiveData<Unit>
    val visiblePasswordLiveData: LiveData<Boolean>
    val numberStatusLiveData: LiveData<Int>
    val errorMessageLiveData: LiveData<String>
    val loadingLiveData: LiveData<Boolean>
    val inputStatusLiveData: LiveData<Boolean>
    val buttonLoginStatusLiveData: LiveData<Boolean>

    fun goMainScreen(phone: String, password: String)
    fun goRegisterScreen()
    fun goForgotPasswordScreen()
    fun goGetStartedScreen()
    fun setVisiblePassword()

    fun checkPhone(phone: String)
}