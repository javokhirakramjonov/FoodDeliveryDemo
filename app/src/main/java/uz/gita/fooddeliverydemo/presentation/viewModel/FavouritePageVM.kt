package uz.gita.fooddeliverydemo.presentation.viewModel

import androidx.lifecycle.LiveData
import uz.gita.fooddeliverydemo.data.foodData.FoodData

interface FavouritePageVM {
    val favouritesLiveData: LiveData<List<FoodData>>
    val errorMessageLiveData: LiveData<String>
    val positiveMessageLiveData: LiveData<String>
    val loadingFavouritesLiveData: LiveData<Boolean>
    val goDescriptionScreenLiveData: LiveData<FoodData>

    fun loadFoods()
    fun loadFavourites(foods: List<FoodData>)
    fun loadFoodCount(foods: List<FoodData>)
    fun addBasket(id: Int)
    fun deleteFavourite(id: Int)
    fun goDescriptionScreen(food: FoodData)
}