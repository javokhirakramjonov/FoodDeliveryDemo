package uz.gita.fooddeliverydemo.domain.repository.impl

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import uz.gita.fooddeliverydemo.domain.repository.LocalRepository
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : LocalRepository {
    private val data = context.getSharedPreferences("DATA", AppCompatActivity.MODE_PRIVATE)

    override fun setLogged(isLogged: Boolean) {
        data.edit().putBoolean("IS_USER_LOGGED", isLogged).apply()
    }

    override fun isUserLogged(): Boolean {
        return data.getBoolean("IS_USER_LOGGED", false)
    }

    override fun setUser(phone: String) {
        data.edit().putString("PHONE", phone).apply()
    }

    override fun getUser(): String {
        return data.getString("PHONE", "")!!
    }
}