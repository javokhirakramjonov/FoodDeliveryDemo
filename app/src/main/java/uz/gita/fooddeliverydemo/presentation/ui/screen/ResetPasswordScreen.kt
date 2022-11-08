package uz.gita.fooddeliverydemo.presentation.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.fooddeliverydemo.R
import uz.gita.fooddeliverydemo.databinding.ScreenResetPasswordBinding
import uz.gita.fooddeliverydemo.presentation.viewModel.ResetPasswordScreenVM
import uz.gita.fooddeliverydemo.presentation.viewModel.impl.ResetPasswordScreenVMImpl
import uz.gita.fooddeliverydemo.utils.snackErrorMessage

@AndroidEntryPoint
class ResetPasswordScreen : Fragment(R.layout.screen_reset_password) {

    private val viewModel: ResetPasswordScreenVM by viewModels<ResetPasswordScreenVMImpl>()
    private val binding by viewBinding(ScreenResetPasswordBinding::bind)
    private val args: ResetPasswordScreenArgs by navArgs()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {

        buttonResetPassword.isEnabled = false
        loader.hide()

        editTextPassword.addTextChangedListener {
            it?.let {
                viewModel.checkPasswords(editTextPassword.text.toString(), editTextConfirmPassword.text.toString())
            }
        }

        editTextConfirmPassword.addTextChangedListener {
            it?.let {
                viewModel.checkPasswords(editTextPassword.text.toString(), editTextConfirmPassword.text.toString())
            }
        }

        buttonResetPassword.setOnClickListener {
            viewModel.resetPassword(args.phone, editTextPassword.text.toString())
        }

        buttonCancel.setOnClickListener {
            viewModel.goBack()
        }

        viewModel.isPasswordsOkLiveData.observe(this@ResetPasswordScreen, passwordsOkObserver)
        viewModel.loadingLiveData.observe(this@ResetPasswordScreen, loadingObserver)
        viewModel.goMainScreenLiveData.observe(this@ResetPasswordScreen, goMainScreenObserver)
        viewModel.goBackLiveData.observe(this@ResetPasswordScreen, goBackScreenObserver)
        viewModel.errorMessageLiveData.observe(this@ResetPasswordScreen, errorMessageObserver)
    }

    private val passwordsOkObserver = Observer<Boolean> {
        binding.buttonResetPassword.isEnabled = it
    }

    private val loadingObserver = Observer<Boolean> {
        if (it)
            binding.loader.show()
        else
            binding.loader.hide()
    }

    private val goMainScreenObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_resetPasswordScreen_to_mainScreen)
    }

    private val goBackScreenObserver = Observer<Unit> {
        findNavController().navigateUp()
    }

    private val errorMessageObserver = Observer<String> {
        snackErrorMessage(it)
    }

}