package uz.gita.fooddeliverydemo.presentation.viewModel

import androidx.lifecycle.LiveData

interface OnBoardingScreenVM {
    val goNextScreenLiveData: LiveData<Unit>
    val goPageScreenLiveData: LiveData<Int>

    fun goNextScreen()
    fun setPage(page: Int)
}