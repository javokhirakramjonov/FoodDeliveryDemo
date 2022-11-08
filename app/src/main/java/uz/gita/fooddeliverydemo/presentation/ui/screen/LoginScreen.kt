package uz.gita.fooddeliverydemo.presentation.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.fooddeliverydemo.R
import uz.gita.fooddeliverydemo.databinding.ScreenLoginBinding
import uz.gita.fooddeliverydemo.presentation.viewModel.LoginScreenVM
import uz.gita.fooddeliverydemo.presentation.viewModel.impl.LoginScreenVMImpl
import uz.gita.fooddeliverydemo.utils.passwordVisible
import uz.gita.fooddeliverydemo.utils.setBackgroundByState
import uz.gita.fooddeliverydemo.utils.snackErrorMessage

@AndroidEntryPoint
class LoginScreen : Fragment(R.layout.screen_login) {

    private val viewModel: LoginScreenVM by viewModels<LoginScreenVMImpl>()
    private val binding by viewBinding(ScreenLoginBinding::bind)

    @SuppressLint("UseCompatLoadingForDrawables", "FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        buttonLogin.isEnabled = false
        loader.hide()

        editTextPhone.addTextChangedListener {
            it?.let {
                viewModel.checkPhone(editTextPhone.text.toString())
            }
        }

        editPassword.addTextChangedListener {
            it?.let {
                viewModel.checkPhone(editTextPhone.text.toString())
            }
        }

        buttonEye.setOnClickListener {
            viewModel.setVisiblePassword()
        }

        buttonForgotPassword.setOnClickListener {
            viewModel.goForgotPasswordScreen()
        }

        buttonLogin.setOnClickListener {
            viewModel.goMainScreen(editTextPhone.text.toString(), editPassword.text.toString())
        }

        buttonRegister.setOnClickListener {
            viewModel.goRegisterScreen()
        }

        buttonCancel.setOnClickListener {
            viewModel.goGetStartedScreen()
        }

        viewModel.loadingLiveData.observe(this@LoginScreen, loadingObserver)
        viewModel.inputStatusLiveData.observe(this@LoginScreen, inputStatusObserver)
        viewModel.buttonLoginStatusLiveData.observe(this@LoginScreen, loginButtonStatusObserver)
        viewModel.numberStatusLiveData.observe(this@LoginScreen, numberCheckObserver)
        viewModel.errorMessageLiveData.observe(this@LoginScreen, messageObserver)
        viewModel.goMainScreenLiveData.observe(this@LoginScreen, goMainScreenObserver)
        viewModel.goRegisterScreenLiveData.observe(this@LoginScreen, goRegisterScreenObserver)
        viewModel.goForgotPasswordScreenLiveData.observe(this@LoginScreen, goForgotPasswordScreenObserver)
        viewModel.goGetStartedScreenLiveData.observe(this@LoginScreen, goGetStartedScreenObserver)
        viewModel.visiblePasswordLiveData.observe(this@LoginScreen, visiblePasswordObserver)
    }

    private val loadingObserver = Observer<Boolean> {
        if (it)
            binding.loader.show()
        else
            binding.loader.hide()
    }

    private val inputStatusObserver = Observer<Boolean> {
        binding.editTextPhone.isEnabled = it
        binding.editPassword.isEnabled = it
    }

    private val loginButtonStatusObserver = Observer<Boolean> {
        binding.buttonLogin.isEnabled = it
    }

    private val numberCheckObserver = Observer<Int> {
        binding.editTextPhone.setBackgroundByState(it, requireContext())
    }

    private val messageObserver = Observer<String> {
        snackErrorMessage(it)
    }

    private val goMainScreenObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_loginScreen_to_mainScreen)
    }
    private val goRegisterScreenObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_loginScreen_to_registerScreen)
    }
    private val goForgotPasswordScreenObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_loginScreen_to_forgotPasswordScreen)
    }
    private val goGetStartedScreenObserver = Observer<Unit> {
        findNavController().popBackStack()
    }
    private val visiblePasswordObserver = Observer<Boolean> {
        binding.editPassword.passwordVisible(it, binding.buttonEye)
    }
}