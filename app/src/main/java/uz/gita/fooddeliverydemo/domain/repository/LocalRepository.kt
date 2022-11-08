package uz.gita.fooddeliverydemo.domain.repository

interface LocalRepository {
    fun setLogged(isLogged: Boolean)
    fun isUserLogged(): Boolean
    fun setUser(phone: String)
    fun getUser(): String
}