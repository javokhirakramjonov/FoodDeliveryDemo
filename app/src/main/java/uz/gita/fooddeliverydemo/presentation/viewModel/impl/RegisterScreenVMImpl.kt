package uz.gita.fooddeliverydemo.presentation.viewModel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.fooddeliverydemo.data.authData.RegisterData
import uz.gita.fooddeliverydemo.domain.useCase.AuthUseCase
import uz.gita.fooddeliverydemo.presentation.viewModel.RegisterScreenVM
import javax.inject.Inject

@HiltViewModel
class RegisterScreenVMImpl @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel(), RegisterScreenVM {
    override val positiveMessageLiveData = MutableLiveData<String>()
    override val errorMessageLiveData = MutableLiveData<String>()
    override val goVerifyScreenLiveData = MutableLiveData<Pair<RegisterData, PhoneAuthProvider.ForceResendingToken>>()
    override val goLoginScreenLiveData = MutableLiveData<Unit>()
    override val goBackLiveData = MutableLiveData<Unit>()
    override val registerByPhoneLiveData = MutableLiveData<String>()
    override val loadingLiveData = MutableLiveData<Boolean>()
    override val registerButtonStatusLiveData = MutableLiveData<Boolean>()
    override val numberStatusLiveData = MutableLiveData<Int>()
    override val fullNameStatusLiveData = MutableLiveData<Int>()
    override val passwordStatusLiveData = MutableLiveData<Int>()
    override val passwordVisibleLiveData = MutableLiveData<Boolean>()
    override val inputEnableLiveData = MutableLiveData<Boolean>()
    private var passwordVisible = false
    private lateinit var fullName: String
    private lateinit var phone: String
    private lateinit var password: String

    private fun checkAllDetails() {
        registerButtonStatusLiveData.value = (passwordStatusLiveData.value == 1 && numberStatusLiveData.value == 1 &&
                fullNameStatusLiveData.value == 1)
    }

    override fun setVisiblePassword() {
        passwordVisible = !passwordVisible
        passwordVisibleLiveData.value = passwordVisible
    }

    override fun checkNumber(phone: String) {
        if (phone.isEmpty())
            numberStatusLiveData.value = 0
        else if ("\\+998[0-9]*".toRegex().matches(phone) && phone.length == 13)
            numberStatusLiveData.value = 1
        else
            numberStatusLiveData.value = -1
        checkAllDetails()
    }

    override fun checkFullName(fullName: String) {
        if (fullName.isEmpty())
            fullNameStatusLiveData.value = 0
        else if (fullName[0] == ' ' || fullName.indexOf("  ") >= 0)
            fullNameStatusLiveData.value = -1
        else
            fullNameStatusLiveData.value = 1
        checkAllDetails()
    }

    override fun checkPassword(password: String) {
        if (password.isEmpty())
            passwordStatusLiveData.value = 0
        else if (password.indexOf(' ') >= 0 || password.length < 6)
            passwordStatusLiveData.value = -1
        else
            passwordStatusLiveData.value = 1
        checkAllDetails()
    }

    override fun goVerifyScreen(fullName: String, phone: String, password: String) {

        inputEnableLiveData.value = false

        this.fullName = fullName
        this.phone = phone
        this.password = password

        loadingLiveData.value = true
        registerButtonStatusLiveData.value = false

        authUseCase.checkUser(phone).onEach {
            it.onSuccess { result ->
                if (result) {
                    loadingLiveData.value = false
                    inputEnableLiveData.value = true
                    errorMessageLiveData.value = "There is a user with this phone number"
                } else {
                    registerByPhoneLiveData.value = phone
                }
            }
            it.onFailure { error ->
                loadingLiveData.value = false
                inputEnableLiveData.value = true
                errorMessageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun goLoginScreen() {
        goLoginScreenLiveData.value = Unit
    }

    override fun goBack() {
        goBackLiveData.value = Unit
    }

    override fun onVerificationFailed(errorMessage: String) {
        inputEnableLiveData.value = true
        errorMessageLiveData.value = errorMessage
    }

    override fun onSuccessfullyCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
        loadingLiveData.value = false
        goVerifyScreenLiveData.value = Pair(RegisterData(fullName, phone, password, verificationId), token)
    }
}