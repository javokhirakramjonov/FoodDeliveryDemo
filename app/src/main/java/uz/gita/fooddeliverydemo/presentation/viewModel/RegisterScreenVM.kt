package uz.gita.fooddeliverydemo.presentation.viewModel

import androidx.lifecycle.LiveData
import com.google.firebase.auth.PhoneAuthProvider
import uz.gita.fooddeliverydemo.data.authData.RegisterData

interface RegisterScreenVM {
    val positiveMessageLiveData: LiveData<String>
    val errorMessageLiveData: LiveData<String>
    val goVerifyScreenLiveData: LiveData<Pair<RegisterData, PhoneAuthProvider.ForceResendingToken>>
    val goLoginScreenLiveData: LiveData<Unit>
    val registerByPhoneLiveData: LiveData<String>
    val loadingLiveData: LiveData<Boolean>
    val registerButtonStatusLiveData: LiveData<Boolean>
    val numberStatusLiveData: LiveData<Int>
    val fullNameStatusLiveData: LiveData<Int>
    val passwordStatusLiveData: LiveData<Int>
    val passwordVisibleLiveData: LiveData<Boolean>
    val inputEnableLiveData: LiveData<Boolean>
    val goBackLiveData: LiveData<Unit>

    fun setVisiblePassword()
    fun checkNumber(phone: String)
    fun checkFullName(fullName: String)
    fun checkPassword(password: String)
    fun goVerifyScreen(fullName: String, phone: String, password: String)
    fun goLoginScreen()
    fun goBack()

    fun onVerificationFailed(errorMessage: String)
    fun onSuccessfullyCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken)
}