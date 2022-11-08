package uz.gita.fooddeliverydemo.domain.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import uz.gita.fooddeliverydemo.data.authData.UserData
import uz.gita.fooddeliverydemo.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) : AuthRepository {

    override suspend fun registerUser(
        fullName: String,
        phone: String,
        password: String,
        success: (Unit) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        fireStore.collection("users").document(phone).set(UserData(fullName, phone, password))
            .addOnSuccessListener {
                success.invoke(Unit)
            }
            .addOnFailureListener {
                failure.invoke(it)
            }
    }

    override suspend fun verifyUser(
        verificationID: String,
        code: String,
        success: (Unit) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        val credential = PhoneAuthProvider.getCredential(verificationID, code)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                success.invoke(Unit)
            }
            .addOnFailureListener {
                failure.invoke(it)
            }
    }

    override suspend fun checkUser(
        phone: String,
        success: (Boolean) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        fireStore.collection("users").whereEqualTo("phone", phone).get()
            .addOnSuccessListener {
                success.invoke(!it.isEmpty)
            }
            .addOnFailureListener {
                failure.invoke(it)
            }
    }

    override suspend fun checkUser(
        phone: String,
        password: String,
        success: (Boolean) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        fireStore.collection("users").whereEqualTo("phone", phone).whereEqualTo("password", password).get()
            .addOnSuccessListener {
                success.invoke(!it.isEmpty)
            }
            .addOnFailureListener {
                failure.invoke(it)
            }
    }

    override suspend fun resetPassword(
        phone: String,
        password: String,
        success: (Unit) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        fireStore.collection("users").document(phone).update("password", password)
            .addOnSuccessListener {
                success.invoke(Unit)
            }
            .addOnFailureListener {
                failure.invoke(it)
            }
    }

    override suspend fun getUserName(
        phone: String,
        success: (String) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        fireStore.collection("users").whereEqualTo("phone", phone).get()
            .addOnSuccessListener {
                success.invoke(it.first().data["fullName"] as String)
            }
            .addOnFailureListener {
                failure.invoke(it)
            }
    }
}