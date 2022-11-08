package uz.gita.fooddeliverydemo.presentation.viewModel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.fooddeliverydemo.domain.repository.LocalRepository
import uz.gita.fooddeliverydemo.domain.useCase.AuthUseCase
import uz.gita.fooddeliverydemo.presentation.viewModel.ResetPasswordScreenVM
import javax.inject.Inject

@HiltViewModel
class ResetPasswordScreenVMImpl @Inject constructor(
    private val localData: LocalRepository,
    private val authUseCase: AuthUseCase
) : ViewModel(), ResetPasswordScreenVM {
    override val goMainScreenLiveData = MutableLiveData<Unit>()
    override val isPasswordsOkLiveData = MutableLiveData<Boolean>()
    override val loadingLiveData = MutableLiveData<Boolean>()
    override val errorMessageLiveData = MutableLiveData<String>()
    override val goBackLiveData = MutableLiveData<Unit>()

    override fun checkPasswords(password1: String, password2: String) {
        isPasswordsOkLiveData.value = (!password1.contains(" ") && password1.length > 5 && password1 == password2)
    }

    override fun resetPassword(phone: String, password: String) {
        loadingLiveData.value = true
        authUseCase.resetPassword(phone, password).onEach {
            loadingLiveData.value = false
            it.onSuccess {
                localData.setLogged(true)
                localData.setUser(phone)
                goMainScreenLiveData.value = Unit
            }
            it.onFailure { error ->
                errorMessageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun goBack() {
        goBackLiveData.value = Unit
    }
}