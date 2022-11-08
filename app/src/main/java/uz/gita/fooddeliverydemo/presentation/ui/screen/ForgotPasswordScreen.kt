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
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.fooddeliverydemo.R
import uz.gita.fooddeliverydemo.data.authData.ResetData
import uz.gita.fooddeliverydemo.databinding.ScreenForgotPasswordBinding
import uz.gita.fooddeliverydemo.presentation.viewModel.ForgotPasswordScreenVM
import uz.gita.fooddeliverydemo.presentation.viewModel.impl.ForgotPasswordScreenVMImpl
import uz.gita.fooddeliverydemo.utils.setBackgroundByState
import uz.gita.fooddeliverydemo.utils.snackErrorMessage
import uz.gita.fooddeliverydemo.utils.snackMessage
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ForgotPasswordScreen : Fragment(R.layout.screen_forgot_password) {

    private val viewModel: ForgotPasswordScreenVM by viewModels<ForgotPasswordScreenVMImpl>()
    private val binding by viewBinding(ScreenForgotPasswordBinding::bind)

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        loader.hide()
        buttonSendCode.isEnabled = false

        editTextPhone.addTextChangedListener {
            it?.let {
                viewModel.checkNumber(editTextPhone.text.toString())
            }
        }

        buttonSendCode.setOnClickListener {
            viewModel.goVerifyScreen(editTextPhone.text.toString())
        }

        viewModel.resetByPhoneLiveData.observe(this@ForgotPasswordScreen, sendCodeObserver)
        viewModel.sendButtonStatusLiveData.observe(this@ForgotPasswordScreen, resetButtonStatusObserver)
        viewModel.positiveMessageLiveData.observe(this@ForgotPasswordScreen, positiveMessageObserver)
        viewModel.numberStatusLiveData.observe(this@ForgotPasswordScreen, inputPhoneStatusObserver)
        viewModel.inputEnableLiveData.observe(this@ForgotPasswordScreen, inputEnableObserver)
        viewModel.goVerifyScreenLiveData.observe(this@ForgotPasswordScreen, goVerifyScreenObserver)
        viewModel.errorMessageLiveData.observe(this@ForgotPasswordScreen, errorMessageObserver)
        viewModel.goBackLiveData.observe(this@ForgotPasswordScreen, goBackObserver)
        viewModel.loadingLiveData.observe(this@ForgotPasswordScreen, loadingObserver)
    }

    private val sendCodeObserver = Observer<String> {
        val auth = Firebase.auth
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(it)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {}

        override fun onVerificationFailed(error: FirebaseException) {
            viewModel.onVerificationFailed(error.message.toString())
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) =
            viewModel.onSuccessfullyCodeSent(verificationId, token)
    }

    private val resetButtonStatusObserver = Observer<Boolean> {
        binding.buttonSendCode.isEnabled = it
    }

    private val positiveMessageObserver = Observer<String> {
        snackMessage(it)
    }

    private val inputPhoneStatusObserver = Observer<Int> {
        binding.editTextPhone.setBackgroundByState(it, requireContext())
    }

    private val inputEnableObserver = Observer<Boolean> {
        binding.editTextPhone.isEnabled = it
    }

    private val goVerifyScreenObserver = Observer<Pair<ResetData, PhoneAuthProvider.ForceResendingToken>> {
        findNavController().navigate(
            ForgotPasswordScreenDirections.actionForgotPasswordScreenToVerifyScreen(
                null,
                it.second,
                it.first
            )
        )
    }

    private val errorMessageObserver = Observer<String> {
        snackErrorMessage(it)
    }

    private val goBackObserver = Observer<Unit> {
        findNavController().navigateUp()
    }

    private val loadingObserver = Observer<Boolean> {
        if (it)
            binding.loader.show()
        else
            binding.loader.hide()
    }
}