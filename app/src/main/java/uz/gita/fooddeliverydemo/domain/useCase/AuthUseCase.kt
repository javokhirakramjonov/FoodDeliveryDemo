package uz.gita.fooddeliverydemo.domain.useCase

import kotlinx.coroutines.flow.Flow

interface AuthUseCase {
    fun registerUser(
        fullName: String,
        phone: String,
        password: String,
    ): Flow<Result<Unit>>

    fun verifyUser(
        code: String,
        verificationID: String
    ): Flow<Result<Unit>>

    fun checkUser(phone: String): Flow<Result<Boolean>>

    fun checkUser(phone: String, password: String): Flow<Result<Boolean>>

    fun resetPassword(phone: String, password: String): Flow<Result<Unit>>

    fun getUsername(phone: String) : Flow<Result<String>>
}