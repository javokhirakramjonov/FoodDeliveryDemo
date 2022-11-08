package uz.gita.fooddeliverydemo.presentation.viewModel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.gita.fooddeliverydemo.domain.repository.LocalRepository
import uz.gita.fooddeliverydemo.domain.useCase.AuthUseCase
import uz.gita.fooddeliverydemo.presentation.viewModel.VerificationScreenVM
import javax.inject.Inject

@HiltViewModel
class VerificationScreenVMImpl @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val localData: LocalRepository
) : ViewModel(), VerificationScreenVM {
    override val goBackLiveData = MutableLiveData<Unit>()
    override val resendLiveData = MutableLiveData<Unit>()
    override val goMainScreenLiveData = MutableLiveData<Unit>()
    override val timeLiveData = MutableLiveData<String>()
    override val verifyButtonState = MutableLiveData<Boolean>()
    override val positiveMessageLiveData = MutableLiveData<String>()
    override val errorMessageLiveData = MutableLiveData<String>()
    override val resendCodeStateLiveData = MutableLiveData<Boolean>()
    override val inputEnableLiveData = MutableLiveData<Boolean>()
    override val loadingLiveData = MutableLiveData<Boolean>()
    override val verifiedLiveData = MutableLiveData<Unit>()
    override val goResetScreenLiveData = MutableLiveData<Unit>()

    private var job: Job? = null
    private val time = 60

    init {
        startTimer(time)
    }

    private fun startTimer(seconds: Int) {
        var minute = seconds / 60
        var second = seconds % 60
        var time = seconds
        job = viewModelScope.launch(Dispatchers.IO) {
            while (time >= 0) {
                timeLiveData.postValue(
                    "${if (minute < 10) "0$minute" else minute}:${if (second < 10) "0$second" else second}"
                )
                delay(1000)
                time--
                minute = time / 60
                second = time % 60
            }
            inputEnableLiveData.postValue(false)
            verifyButtonState.postValue(false)
            resendCodeStateLiveData.postValue(true)
            errorMessageLiveData.postValue("Time is over, go back or press RESEND")
            job?.cancel()
        }
    }

    override fun register(
        fullName: String,
        phone: String,
        password: String
    ) {
        loadingLiveData.value = true
        inputEnableLiveData.value = false
        verifyButtonState.value = false
        resendCodeStateLiveData.value = false
        authUseCase.registerUser(fullName, phone, password).onEach {
            it.onSuccess {
                job?.cancel()
                localData.setLogged(true)
                localData.setUser(phone)
                goMainScreenLiveData.value = Unit
            }
            it.onFailure { error ->
                loadingLiveData.value = false
                inputEnableLiveData.value = true
                errorMessageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun verify(
        code: String,
        verificationID: String,
    ) {
        loadingLiveData.value = true
        inputEnableLiveData.value = false
        verifyButtonState.value = false
        resendCodeStateLiveData.value = false
        authUseCase.verifyUser(code, verificationID).onEach {
            it.onSuccess {
                job?.cancel()
                verifiedLiveData.value = Unit
            }
            it.onFailure { error ->
                loadingLiveData.value = false
                inputEnableLiveData.value = true
                errorMessageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun checkCode(code: String) {
        verifyButtonState.value = (code.length == 6 && "[0-9]*".toRegex().matches(code))
    }

    override fun resendCode() {
        resendCodeStateLiveData.value = false
        loadingLiveData.value = true
        resendLiveData.value = Unit
    }

    override fun codeSent() {
        startTimer(time)
        loadingLiveData.value = false
        inputEnableLiveData.value = true
    }

    override fun goBack() {
        job?.cancel()
        goBackLiveData.value = Unit
    }

    override fun error(message: String) {
        errorMessageLiveData.value = message
    }

    override fun cancel() {
        job?.cancel()
    }

    override fun goResetScreen() {
        goResetScreenLiveData.value = Unit
    }
}