package uz.gita.fooddeliverydemo.presentation.viewModel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.gita.fooddeliverydemo.presentation.viewModel.OnBoardingScreenVM

class OnBoardingScreenVMImpl : ViewModel(), OnBoardingScreenVM {
    override val goNextScreenLiveData = MutableLiveData<Unit>()
    override val goPageScreenLiveData = MutableLiveData<Int>()
    private var pager = 1

    override fun goNextScreen() {
        if (pager < 3)
            goPageScreenLiveData.value = pager++
        else
            goNextScreenLiveData.value = Unit
    }

    override fun setPage(page: Int) {
        pager = page
        goNextScreen()
    }
}