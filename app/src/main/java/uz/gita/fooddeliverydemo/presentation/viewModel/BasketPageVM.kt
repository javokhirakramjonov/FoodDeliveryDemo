package uz.gita.fooddeliverydemo.presentation.viewModel

import androidx.lifecycle.LiveData
import uz.gita.fooddeliverydemo.data.foodData.FoodData

interface BasketPageVM {
    //    val goCheckoutScreenLiveData: LiveData<> todo
    val foodsLiveData: LiveData<List<FoodData>>
    val errorMessageLiveData: LiveData<String>
    val goDescriptionScreenLiveData: LiveData<FoodData>
    val loadingLiveData: LiveData<Boolean>
    val costLiveData: LiveData<Double>
    val oldCountLiveData: LiveData<Pair<Int, Int>>

    //    fun goCheckoutScreen() todo
    fun loadFoods()
    fun changeCountFood(id: Int, count: Int)
    fun deleteFoodFromBasket(id: Int)
    fun goDescriptionScreen(food: FoodData)
}