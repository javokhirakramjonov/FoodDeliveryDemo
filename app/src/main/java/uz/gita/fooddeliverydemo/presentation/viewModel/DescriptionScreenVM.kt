package uz.gita.fooddeliverydemo.presentation.viewModel

import androidx.lifecycle.LiveData

interface DescriptionScreenVM {
    val countLiveData: LiveData<Int>
    val goBackLiveData: LiveData<Unit>
    val subTotalLiveData: LiveData<String>
    val errorMessageLiveData: LiveData<String>
    val addBasketStateLiveData: LiveData<Boolean>
    val decrementStateLiveData: LiveData<Boolean>
    val counterButtonsStateLiveData: LiveData<Boolean>

    fun goBack()
    fun loader(count: Int, cost: Double, state: Boolean)
    fun addToBasket(id: Int, count: Int, cost: Double, oldCount: Int, state: Boolean)
}