package uz.gita.fooddeliverydemo.presentation.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
import uz.gita.fooddeliverydemo.data.authData.ResetData
import uz.gita.fooddeliverydemo.databinding.ScreenSendCodeBinding
import uz.gita.fooddeliverydemo.presentation.viewModel.VerificationScreenVM
import uz.gita.fooddeliverydemo.presentation.viewModel.impl.VerificationScreenVMImpl
import uz.gita.fooddeliverydemo.utils.snackErrorMessage
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class VerifyScreen : Fragment(R.layout.screen_send_code) {

    private val viewModel: VerificationScreenVM by viewModels<VerificationScreenVMImpl>()
    private val binding by viewBinding(ScreenSendCodeBinding::bind)
    private val args: VerifyScreenArgs by navArgs()
    private var user: RegisterData? = null
    private lateinit var token: PhoneAuthProvider.ForceResendingToken
    private var resetData: ResetData? = null

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {

        user = args.user
        token = args.token
        resetData = args.userForgotPassword

        loader.hide()
        buttonVerifyCode.isEnabled = false
        buttonResend.isEnabled = false
        buttonResend.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))

        buttonVerifyCode.setOnClickListener {
            user?.let {
                viewModel.verify(editTextCode.text.toString(), it.verificationId)
            }
            resetData?.let {
                viewModel.verify(editTextCode.text.toString(), it.verificationId)
            }
        }

        editTextCode.addTextChangedListener {
            it?.let {
                viewModel.checkCode(editTextCode.text.toString())
            }
        }

        buttonResend.setOnClickListener {
            viewModel.resendCode()
        }

        buttonCancel.setOnClickListener {
            viewModel.goBack()
        }

        viewModel.goResetScreenLiveData.observe(this@VerifyScreen, goResetScreenObserver)
        viewModel.verifiedLiveData.observe(this@VerifyScreen, verifiedObserver)
        viewModel.resendLiveData.observe(this@VerifyScreen, resendObserver)
        viewModel.verifyButtonState.observe(this@VerifyScreen, verifyButtonStateObserver)
        viewModel.goMainScreenLiveData.observe(this@VerifyScreen, goMainScreenObserver)
        viewModel.resendCodeStateLiveData.observe(this@VerifyScreen, resendButtonEnableObserver)
        viewModel.goBackLiveData.observe(this@VerifyScreen, goBackObserver)
        viewModel.inputEnableLiveData.observe(this@VerifyScreen, inputEnableObserver)
        viewModel.loadingLiveData.observe(this@VerifyScreen, loadingObserver)
        viewModel.errorMessageLiveData.observe(this@VerifyScreen, errorMessageObserver)
        viewModel.timeLiveData.observe(this@VerifyScreen, timeObserver)
    }

    private val goResetScreenObserver = Observer<Unit> {
        findNavController().navigate(VerifyScreenDirections.actionVerifyScreenToResetPasswordScreen(resetData!!.phone))
    }

    private val verifiedObserver = Observer<Unit> {
        user?.let {
            viewModel.register(it.fullName, it.phone, it.password)
        }
        resetData?.let {
            viewModel.goResetScreen()
        }
    }

    private val resendObserver = Observer<Unit> {
        val auth = Firebase.auth
        val phone = if (user != null) user!!.phone else resetData!!.phone
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .setForceResendingToken(token)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {}

        override fun onVerificationFailed(error: FirebaseException) {
            viewModel.error(error.message.toString())
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            user?.verificationId = verificationId
            resetData?.verificationId = verificationId
            this@VerifyScreen.token = token
            viewModel.codeSent()
        }
    }

    private val verifyButtonStateObserver = Observer<Boolean> {
        binding.buttonVerifyCode.isEnabled = it
    }

    private val goMainScreenObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_verifyScreen_to_mainScreen)
    }

    private val resendButtonEnableObserver = Observer<Boolean> {
        binding.buttonResend.isEnabled = it
        if (it)
            binding.buttonResend.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
        else
            binding.buttonResend.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
    }

    private val goBackObserver = Observer<Unit> {
        findNavController().navigateUp()
    }

    private val inputEnableObserver = Observer<Boolean> {
        binding.editTextCode.isEnabled = it
    }

    private val loadingObserver = Observer<Boolean> {
        if (it)
            binding.loader.show()
        else
            binding.loader.hide()
    }

    private val errorMessageObserver = Observer<String> {
        snackErrorMessage(it)
    }

    private val timeObserver = Observer<String> {
        binding.time.text = it
    }
}