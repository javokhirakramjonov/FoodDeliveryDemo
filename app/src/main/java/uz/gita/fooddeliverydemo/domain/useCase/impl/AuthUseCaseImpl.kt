package uz.gita.fooddeliverydemo.domain.useCase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import uz.gita.fooddeliverydemo.domain.repository.AuthRepository
import uz.gita.fooddeliverydemo.domain.useCase.AuthUseCase
import javax.inject.Inject

class AuthUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : AuthUseCase {
    override fun registerUser(
        fullName: String,
        phone: String,
        password: String
    ) = callbackFlow<Result<Unit>> {
        authRepository.registerUser(
            fullName,
            phone,
            password,
            {
                trySend(Result.success(Unit))
            },
            {
                trySend(Result.failure(it))
            }
        )
        awaitClose {}
    }.flowOn(Dispatchers.IO)

    override fun verifyUser(
        code: String,
        verificationID: String
    ) = callbackFlow<Result<Unit>> {
        authRepository.verifyUser(
            verificationID,
            code,
            {
                trySend(Result.success(Unit))
            },
            {
                trySend(Result.failure(it))
            }
        )
        awaitClose {}
    }.flowOn(Dispatchers.IO)

    override fun checkUser(phone: String) = callbackFlow<Result<Boolean>> {
        authRepository.checkUser(
            phone,
            {
                trySend(Result.success(it))
            },
            {
                trySend(Result.failure(it))
            }
        )
        awaitClose {}
    }.flowOn(Dispatchers.IO)

    override fun checkUser(phone: String, password: String) = callbackFlow<Result<Boolean>> {
        authRepository.checkUser(
            phone,
            password,
            {
                trySend(Result.success(it))
            },
            {
                trySend(Result.failure(it))
            }
        )
        awaitClose {}
    }.flowOn(Dispatchers.IO)

    override fun resetPassword(phone: String, password: String) = callbackFlow<Result<Unit>> {
        authRepository.resetPassword(
            phone,
            password,
            {
                trySend(Result.success(Unit))
            },
            {
                trySend(Result.failure(it))
            }
        )
        awaitClose {}
    }.flowOn(Dispatchers.IO)

    override fun getUsername(phone: String) = callbackFlow<Result<String>> {
        authRepository.getUserName(
            phone,
            {
                trySend(Result.success(it))
            },
            {
                trySend(Result.failure(it))
            }
        )
        awaitClose{}
    }.flowOn(Dispatchers.IO)
}