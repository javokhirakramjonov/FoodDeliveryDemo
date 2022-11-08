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
import uz.gita.fooddeliverydemo.data.authData.RegisterData
import uz.gita.fooddeliverydemo.databinding.ScreenRegisterBinding
import uz.gita.fooddeliverydemo.presentation.viewModel.RegisterScreenVM
import uz.gita.fooddeliverydemo.presentation.viewModel.impl.RegisterScreenVMImpl
import uz.gita.fooddeliverydemo.utils.passwordVisible
import uz.gita.fooddeliverydemo.utils.setBackgroundByState
import uz.gita.fooddeliverydemo.utils.snackErrorMessage
import uz.gita.fooddeliverydemo.utils.snackMessage
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class RegisterScreen : Fragment(R.layout.screen_register) {

    private val viewModel: RegisterScreenVM by viewModels<RegisterScreenVMImpl>()
    private val binding by viewBinding(ScreenRegisterBinding::bind)

    @SuppressLint("FragmentLiveDataObserve", "UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        loader.hide()

        buttonRegister.setOnClickListener {
            viewModel.goVerifyScreen(
                editTextFullName.text.toString(),
                editTextPhone.text.toString(),
                editTextPassword.text.toString()
            )
        }
        editTextFullName.addTextChangedListener {
            it?.let {
                viewModel.checkFullName(editTextFullName.text.toString())
            }
        }
        editTextPhone.addTextChangedListener {
            it?.let {
                viewModel.checkNumber(editTextPhone.text.toString())
            }
        }
        buttonEye.setOnClickListener {
            viewModel.setVisiblePassword()
        }
        editTextPassword.addTextChangedListener {
            it?.let {
                viewModel.checkPassword(editTextPassword.text.toString())
            }
        }
        buttonLogin.setOnClickListener {
            viewModel.goLoginScreen()
        }
        buttonCancel.setOnClickListener {
            viewModel.goBack()
        }

        viewModel.goBackLiveData.observe(this@RegisterScreen, goBackObserver)
        viewModel.loadingLiveData.observe(this@RegisterScreen, loadingObserver)
        viewModel.inputEnableLiveData.observe(this@RegisterScreen, inputEnableObserver)
        viewModel.registerButtonStatusLiveData.observe(this@RegisterScreen, registerButtonStateObserver)
        viewModel.passwordVisibleLiveData.observe(this@RegisterScreen, passwordVisibleObserver)
        viewModel.passwordStatusLiveData.observe(this@RegisterScreen, passwordObserver)
        viewModel.numberStatusLiveData.observe(this@RegisterScreen, phoneObserver)
        viewModel.fullNameStatusLiveData.observe(this@RegisterScreen, fullNameObserver)
        viewModel.goVerifyScreenLiveData.observe(this@RegisterScreen, goVerifyScreenObserver)
        viewModel.goLoginScreenLiveData.observe(this@RegisterScreen, goLoginObserver)
        viewModel.registerByPhoneLiveData.observe(this@RegisterScreen, registerObserver)
        viewModel.positiveMessageLiveData.observe(this@RegisterScreen, positiveMessageObserver)
        viewModel.errorMessageLiveData.observe(this@RegisterScreen, errorMessageObserver)
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

    private val inputEnableObserver = Observer<Boolean> {
        binding.editTextFullName.isEnabled = it
        binding.editTextPhone.isEnabled = it
        binding.editTextPassword.isEnabled = it
    }

    private val registerButtonStateObserver = Observer<Boolean> {
        binding.buttonRegister.isEnabled = it
    }

    private val passwordVisibleObserver = Observer<Boolean> {
        binding.editTextPassword.passwordVisible(it, binding.buttonEye)
    }

    private val passwordObserver = Observer<Int> {
        binding.editTextPassword.setBackgroundByState(it, requireContext())
    }

    private val phoneObserver = Observer<Int> {
        binding.editTextPhone.setBackgroundByState(it, requireContext())
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private val fullNameObserver = Observer<Int> {
        binding.editTextFullName.setBackgroundByState(it, requireContext())
    }

    private val goVerifyScreenObserver = Observer<Pair<RegisterData, PhoneAuthProvider.ForceResendingToken>> {
        findNavController().navigate(
            RegisterScreenDirections.actionRegisterScreenToVerifyScreen(
                it.first,
                it.second,
                null
            )
        )
    }

    private val goLoginObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_registerScreen_to_loginScreen)
    }

    private val positiveMessageObserver = Observer<String> {
        snackMessage(it)
    }

    private val errorMessageObserver = Observer<String> {
        snackErrorMessage(it)
    }

    private val registerObserver = Observer<String> { phone ->
        val auth = Firebase.auth
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
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
}