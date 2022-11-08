package uz.gita.fooddeliverydemo.presentation.viewModel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.fooddeliverydemo.domain.repository.LocalRepository
import uz.gita.fooddeliverydemo.domain.useCase.AuthUseCase
import uz.gita.fooddeliverydemo.presentation.viewModel.LoginScreenVM
import javax.inject.Inject

@HiltViewModel
class LoginScreenVMImpl @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val localData: LocalRepository
) : ViewModel(), LoginScreenVM {
    override val goMainScreenLiveData = MutableLiveData<Unit>()
    override val goRegisterScreenLiveData = MutableLiveData<Unit>()
    override val goForgotPasswordScreenLiveData = MutableLiveData<Unit>()
    override val goGetStartedScreenLiveData = MutableLiveData<Unit>()
    override val visiblePasswordLiveData = MutableLiveData<Boolean>()
    override val loadingLiveData = MutableLiveData<Boolean>()
    override val inputStatusLiveData = MutableLiveData<Boolean>()
    override val buttonLoginStatusLiveData = MutableLiveData<Boolean>()
    override val numberStatusLiveData = MutableLiveData<Int>()
    override val errorMessageLiveData = MutableLiveData<String>()
    private var visiblePassword = false

    override fun goMainScreen(phone: String, password: String) {
        loadingLiveData.value = true
        inputStatusLiveData.value = false
        buttonLoginStatusLiveData.value = false
        authUseCase.checkUser(phone, password).onEach {
            it.onSuccess { result ->
                if (result) {
                    localData.setUser(phone)
                    localData.setLogged(true)
                    goMainScreenLiveData.value = Unit
                } else {
                    loadingLiveData.value = false
                    inputStatusLiveData.value = true
                    errorMessageLiveData.value = "phone or password is invalid"
                }
            }
            it.onFailure { error ->
                loadingLiveData.value = false
                errorMessageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun goRegisterScreen() {
        goRegisterScreenLiveData.value = Unit
    }

    override fun goForgotPasswordScreen() {
        goForgotPasswordScreenLiveData.value = Unit
    }

    override fun goGetStartedScreen() {
        goGetStartedScreenLiveData.value = Unit
    }

    override fun setVisiblePassword() {
        visiblePassword = !visiblePassword
        visiblePasswordLiveData.value = visiblePassword
    }

    override fun checkPhone(phone: String) {
        if (phone.isEmpty()) {
            buttonLoginStatusLiveData.value = false
            numberStatusLiveData.value = 0
        } else if (phone.length == 13 && "\\+998[0-9]*".toRegex().matches(phone)) {
            buttonLoginStatusLiveData.value = true
            numberStatusLiveData.value = 1
        } else {
            buttonLoginStatusLiveData.value = false
            numberStatusLiveData.value = -1
        }
    }
}