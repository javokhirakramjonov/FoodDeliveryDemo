package uz.gita.fooddeliverydemo.presentation.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.fooddeliverydemo.R
import uz.gita.fooddeliverydemo.databinding.ScreenOnboardingBinding
import uz.gita.fooddeliverydemo.presentation.ui.adapter.OnBoardingScreenPagerAdapter
import uz.gita.fooddeliverydemo.presentation.viewModel.OnBoardingScreenVM
import uz.gita.fooddeliverydemo.presentation.viewModel.impl.OnBoardingScreenVMImpl

class OnBoardingScreen : Fragment(R.layout.screen_onboarding) {

    private val viewModel: OnBoardingScreenVM by viewModels<OnBoardingScreenVMImpl>()
    private val binding by viewBinding(ScreenOnboardingBinding::bind)
    private val adapter = OnBoardingScreenPagerAdapter()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        viewPager.adapter = adapter

        buttonNext.setOnClickListener {
            viewModel.goNextScreen()
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.setPage(position)
            }
        })

        viewModel.goPageScreenLiveData.observe(this@OnBoardingScreen, goPageObserver)
        viewModel.goNextScreenLiveData.observe(this@OnBoardingScreen, goNextScreenObserver)
    }

    private val goPageObserver = Observer<Int> {
        if (it == 2)
            binding.buttonNext.setText(R.string.text_get_started)
        else
            binding.buttonNext.setText(R.string.text_next)
        if (binding.viewPager.currentItem != it)
            binding.viewPager.setCurrentItem(it, true)
    }

    private val goNextScreenObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_onBoardingScreen_to_getStartedScreen)
    }

}