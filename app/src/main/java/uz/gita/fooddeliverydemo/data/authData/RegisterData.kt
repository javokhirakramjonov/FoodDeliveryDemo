package uz.gita.fooddeliverydemo.data.authData

import java.io.Serializable

data class RegisterData(
    val fullName: String,
    val phone: String,
    val password: String,
    var verificationId: String,
) : Serializable