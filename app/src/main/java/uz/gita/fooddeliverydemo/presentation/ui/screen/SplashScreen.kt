package uz.gita.fooddeliverydemo.presentation.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.fooddeliverydemo.R
import uz.gita.fooddeliverydemo.databinding.ScreenSplashBinding
import uz.gita.fooddeliverydemo.presentation.viewModel.SplashScreenVM
import uz.gita.fooddeliverydemo.presentation.viewModel.impl.SplashScreenVMImpl

@AndroidEntryPoint
class SplashScreen : Fragment(R.layout.screen_splash) {

    private val viewModel: SplashScreenVM by viewModels<SplashScreenVMImpl>()
    private val binding by viewBinding(ScreenSplashBinding::bind)

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        viewModel.goOnBoardingScreen()

        viewModel.goMainScreenLiveData.observe(this@SplashScreen, goMainScreenObserver)
        viewModel.goOnboardScreenLiveData.observe(this@SplashScreen, goOnBoardingScreenObserver)
    }

    private val goMainScreenObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_splashScreen_to_mainScreen)
    }

    private val goOnBoardingScreenObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_splashScreen_to_onBoardingScreen)
    }

}