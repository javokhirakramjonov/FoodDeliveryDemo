package uz.gita.fooddeliverydemo.presentation.viewModel

import androidx.lifecycle.LiveData

interface VerificationScreenVM {
    val goBackLiveData: LiveData<Unit>
    val goMainScreenLiveData: LiveData<Unit>
    val timeLiveData: LiveData<String>
    val verifyButtonState: LiveData<Boolean>
    val positiveMessageLiveData: LiveData<String>
    val errorMessageLiveData: LiveData<String>
    val resendCodeStateLiveData: LiveData<Boolean>
    val inputEnableLiveData: LiveData<Boolean>
    val loadingLiveData: LiveData<Boolean>
    val resendLiveData: LiveData<Unit>
    val verifiedLiveData: LiveData<Unit>
    val goResetScreenLiveData: LiveData<Unit>

    fun register(
        fullName: String,
        phone: String,
        password: String
    )

    fun verify(
        code: String,
        verificationID: String
    )

    fun checkCode(code: String)
    fun resendCode()
    fun codeSent()
    fun goBack()
    fun error(message: String)
    fun cancel()
    fun goResetScreen()
}