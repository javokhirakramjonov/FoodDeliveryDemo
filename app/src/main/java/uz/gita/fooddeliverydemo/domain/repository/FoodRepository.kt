package uz.gita.fooddeliverydemo.domain.repository

import uz.gita.fooddeliverydemo.data.foodData.AdsData
import uz.gita.fooddeliverydemo.data.foodData.FoodData
import uz.gita.fooddeliverydemo.data.foodData.RestaurantData
import uz.gita.fooddeliverydemo.data.foodData.SearchData

interface FoodRepository {
    suspend fun getAds(
        success: (List<AdsData>) -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun getRestaurants(
        success: (List<RestaurantData>) -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun getFoods(
        success: (List<FoodData>) -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun getFoodsByRestaurant(
        list: List<Int>,
        success: (List<FoodData>) -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun setFavourite(
        id: Int,
        liked: Boolean,
        success: (Unit) -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun getFavourite(
        success: (List<Int>) -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun changeCountFood(
        id: Int,
        count: Int,
        success: (Unit) -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun getFoodCount(
        success: (List<Pair<Int, Int>>) -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun getFoodBySearch(
        text: String,
        success: (List<FoodData>) -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun getRecentSearchedFoods(
        success: (List<SearchData>) -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun addFoodToRecentSearched(
        data: SearchData,
        success: (Unit) -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun deleteFoodFromRecentSearched(
        data: SearchData,
        success: (Unit) -> Unit,
        failure: (Throwable) -> Unit
    )
}