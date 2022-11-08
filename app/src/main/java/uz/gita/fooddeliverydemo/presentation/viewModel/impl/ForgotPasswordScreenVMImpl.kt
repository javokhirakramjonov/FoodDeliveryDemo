package uz.gita.fooddeliverydemo.presentation.viewModel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.fooddeliverydemo.data.authData.ResetData
import uz.gita.fooddeliverydemo.domain.useCase.AuthUseCase
import uz.gita.fooddeliverydemo.presentation.viewModel.ForgotPasswordScreenVM
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordScreenVMImpl @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel(), ForgotPasswordScreenVM {
    override val positiveMessageLiveData = MutableLiveData<String>()
    override val errorMessageLiveData = MutableLiveData<String>()
    override val goVerifyScreenLiveData = MutableLiveData<Pair<ResetData, PhoneAuthProvider.ForceResendingToken>>()
    override val resetByPhoneLiveData = MutableLiveData<String>()
    override val goBackLiveData = MutableLiveData<Unit>()
    override val loadingLiveData = MutableLiveData<Boolean>()
    override val sendButtonStatusLiveData = MutableLiveData<Boolean>()
    override val numberStatusLiveData = MutableLiveData<Int>()
    override val inputEnableLiveData = MutableLiveData<Boolean>()
    private lateinit var phone: String

    override fun checkNumber(phone: String) {
        if (phone.isEmpty()) {
            numberStatusLiveData.value = 0
            sendButtonStatusLiveData.value = false
        } else if ("\\+998[0-9]*".toRegex().matches(phone) && phone.length == 13) {
            numberStatusLiveData.value = 1
            sendButtonStatusLiveData.value = true
        } else {
            sendButtonStatusLiveData.value = false
            numberStatusLiveData.value = -1
        }
    }

    override fun goVerifyScreen(phone: String) {

        inputEnableLiveData.value = false

        this.phone = phone

        loadingLiveData.value = true
        sendButtonStatusLiveData.value = false

        authUseCase.checkUser(phone).onEach {

            it.onSuccess { result ->
                if (!result) {
                    inputEnableLiveData.value = true
                    errorMessageLiveData.value = "There is no user with this phone number"
                    loadingLiveData.value = false
                } else
                    resetByPhoneLiveData.value = phone
            }
            it.onFailure { error ->
                inputEnableLiveData.value = true
                loadingLiveData.value = false
                errorMessageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun goBack() {
        goBackLiveData.value = Unit
    }

    override fun onVerificationFailed(errorMessage: String) {
        inputEnableLiveData.value = true
        errorMessageLiveData.value = errorMessage
    }

    override fun onSuccessfullyCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
        goVerifyScreenLiveData.value = Pair(ResetData(phone, verificationId), token)
    }
}