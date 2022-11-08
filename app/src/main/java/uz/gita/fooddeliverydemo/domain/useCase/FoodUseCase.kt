package uz.gita.fooddeliverydemo.domain.useCase

import kotlinx.coroutines.flow.Flow
import uz.gita.fooddeliverydemo.data.foodData.AdsData
import uz.gita.fooddeliverydemo.data.foodData.FoodData
import uz.gita.fooddeliverydemo.data.foodData.RestaurantData
import uz.gita.fooddeliverydemo.data.foodData.SearchData

interface FoodUseCase {
    fun getAds(): Flow<Result<List<AdsData>>>
    fun getRestaurants(): Flow<Result<List<RestaurantData>>>
    fun getFoods(): Flow<Result<List<FoodData>>>
    fun getFoodsByRestaurant(restaurants: List<RestaurantData>): Flow<Result<List<FoodData>>>
    fun getFavourites(): Flow<Result<List<Int>>>
    fun setFavourite(id: Int, liked: Boolean): Flow<Result<Unit>>
    fun changeCountFood(id: Int, count: Int): Flow<Result<Unit>>
    fun getFoodCount(): Flow<Result<List<Pair<Int, Int>>>>
    fun getFoodBySearch(text: String): Flow<Result<List<FoodData>>>
    fun getFoodsSearched(): Flow<Result<List<SearchData>>>
    fun addFoodToSearched(data: SearchData): Flow<Result<Unit>>
    fun deleteFoodFromSearched(data: SearchData): Flow<Result<Unit>>
}