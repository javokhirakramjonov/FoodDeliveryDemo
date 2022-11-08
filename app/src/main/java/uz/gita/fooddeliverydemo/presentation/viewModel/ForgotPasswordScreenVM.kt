package uz.gita.fooddeliverydemo.presentation.viewModel

import androidx.lifecycle.LiveData
import com.google.firebase.auth.PhoneAuthProvider
import uz.gita.fooddeliverydemo.data.authData.ResetData

interface ForgotPasswordScreenVM {
    val positiveMessageLiveData: LiveData<String>
    val errorMessageLiveData: LiveData<String>
    val goVerifyScreenLiveData: LiveData<Pair<ResetData, PhoneAuthProvider.ForceResendingToken>>
    val resetByPhoneLiveData: LiveData<String>
    val loadingLiveData: LiveData<Boolean>
    val sendButtonStatusLiveData: LiveData<Boolean>
    val numberStatusLiveData: LiveData<Int>
    val inputEnableLiveData: LiveData<Boolean>
    val goBackLiveData: LiveData<Unit>

    fun checkNumber(phone: String)
    fun goVerifyScreen(phone: String)
    fun goBack()

    fun onVerificationFailed(errorMessage: String)
    fun onSuccessfullyCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken)
}