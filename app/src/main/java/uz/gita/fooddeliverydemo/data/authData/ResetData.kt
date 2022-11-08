package uz.gita.fooddeliverydemo.data.authData

import java.io.Serializable

data class ResetData(
    val phone: String,
    var verificationId: String
) : Serializable