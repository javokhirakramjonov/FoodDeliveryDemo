package uz.gita.fooddeliverydemo.presentation.viewModel

import androidx.lifecycle.LiveData

interface GetStartedScreenVM {
    val goLoginScreenLiveData: LiveData<Unit>
    val goRegisterScreenLiveData: LiveData<Unit>

    fun goLoginScreen()
    fun goRegisterScreen()
}