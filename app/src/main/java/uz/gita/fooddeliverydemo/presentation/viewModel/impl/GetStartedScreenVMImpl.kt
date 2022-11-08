package uz.gita.fooddeliverydemo.presentation.viewModel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.gita.fooddeliverydemo.presentation.viewModel.GetStartedScreenVM

class GetStartedScreenVMImpl : ViewModel(), GetStartedScreenVM {
    override val goLoginScreenLiveData = MutableLiveData<Unit>()
    override val goRegisterScreenLiveData = MutableLiveData<Unit>()

    override fun goLoginScreen() {
        goLoginScreenLiveData.value = Unit
    }

    override fun goRegisterScreen() {
        goRegisterScreenLiveData.value = Unit
    }
}