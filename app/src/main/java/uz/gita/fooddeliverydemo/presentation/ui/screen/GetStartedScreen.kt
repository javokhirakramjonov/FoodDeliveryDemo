package uz.gita.fooddeliverydemo.presentation.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.fooddeliverydemo.R
import uz.gita.fooddeliverydemo.databinding.ScreenGetStartedBinding
import uz.gita.fooddeliverydemo.presentation.viewModel.GetStartedScreenVM
import uz.gita.fooddeliverydemo.presentation.viewModel.impl.GetStartedScreenVMImpl

class GetStartedScreen : Fragment(R.layout.screen_get_started) {

    private val viewModel: GetStartedScreenVM by viewModels<GetStartedScreenVMImpl>()
    private val binding by viewBinding(ScreenGetStartedBinding::bind)

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        buttonLogin.setOnClickListener {
            viewModel.goLoginScreen()
        }

        buttonRegister.setOnClickListener {
            viewModel.goRegisterScreen()
        }

        viewModel.goLoginScreenLiveData.observe(this@GetStartedScreen, goLoginScreenObserver)
        viewModel.goRegisterScreenLiveData.observe(this@GetStartedScreen, goRegisterScreenObserver)
    }

    private val goLoginScreenObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_getStartedScreen_to_loginScreen)
    }

    private val goRegisterScreenObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_getStartedScreen_to_registerScreen)
    }

}