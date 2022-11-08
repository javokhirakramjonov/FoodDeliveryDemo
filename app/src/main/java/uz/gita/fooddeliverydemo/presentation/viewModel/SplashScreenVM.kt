package uz.gita.fooddeliverydemo.presentation.viewModel

import androidx.lifecycle.LiveData

interface SplashScreenVM {
    val goOnboardScreenLiveData: LiveData<Unit>
    val goMainScreenLiveData: LiveData<Unit>

    fun goOnBoardingScreen()
}