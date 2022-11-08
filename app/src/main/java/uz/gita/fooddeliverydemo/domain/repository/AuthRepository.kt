package uz.gita.fooddeliverydemo.domain.repository

interface AuthRepository {
    suspend fun registerUser(
        fullName: String,
        phone: String,
        password: String,
        success: (Unit) -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun verifyUser(
        verificationID: String,
        code: String,
        success: (Unit) -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun checkUser(
        phone: String,
        success: (Boolean) -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun checkUser(
        phone: String,
        password: String,
        success: (Boolean) -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun resetPassword(
        phone: String,
        password: String,
        success: (Unit) -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun getUserName(
        phone: String,
        success: (String) -> Unit,
        failure: (Throwable) -> Unit
    )
}