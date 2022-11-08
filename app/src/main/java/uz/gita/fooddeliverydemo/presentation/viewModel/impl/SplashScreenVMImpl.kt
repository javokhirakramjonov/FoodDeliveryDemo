package uz.gita.fooddeliverydemo.presentation.viewModel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.gita.fooddeliverydemo.domain.repository.LocalRepository
import uz.gita.fooddeliverydemo.presentation.viewModel.SplashScreenVM
import javax.inject.Inject

@HiltViewModel
class SplashScreenVMImpl @Inject constructor(
    private val localData: LocalRepository
) : ViewModel(), SplashScreenVM {
    override val goOnboardScreenLiveData = MutableLiveData<Unit>()
    override val goMainScreenLiveData = MutableLiveData<Unit>()

    override fun goOnBoardingScreen() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(2000)
            if (localData.isUserLogged())
                goMainScreenLiveData.postValue(Unit)
            else
                goOnboardScreenLiveData.postValue(Unit)
        }
    }
}