package uz.gita.fooddeliverydemo.presentation.viewModel

import androidx.lifecycle.LiveData
import uz.gita.fooddeliverydemo.data.foodData.AdsData
import uz.gita.fooddeliverydemo.data.foodData.FoodData
import uz.gita.fooddeliverydemo.data.foodData.RestaurantData

interface HomePageVM {
    val adsLiveData: LiveData<List<AdsData>>
    val restaurantLiveData: LiveData<List<RestaurantData>>
    val foodsLiveData: LiveData<List<FoodData>>
    val messageLiveData: LiveData<String>
    val loadingFoodLiveData: LiveData<Boolean>
    val loadingRestaurantLiveData: LiveData<Boolean>
    val adsIndexLiveData: LiveData<Int>
    val userNameLiveData: LiveData<String>
    val goDescriptionScreenLiveData: LiveData<FoodData>

    fun loadAds()
    fun loadRestaurants()
    fun loadFoods()
    fun startAds(position: Int)
    fun selectUnselectRestaurant(restaurants: List<RestaurantData>)
    fun setFavouriteFood(food: FoodData)
    fun loadFavouriteFood(foods: List<FoodData>)
    fun loadFoodCount(foods: List<FoodData>)
    fun loadUserName()
    fun refreshFavourites()
    fun goDescriptionScreen(food: FoodData)
    fun addBasket(id: Int)
}