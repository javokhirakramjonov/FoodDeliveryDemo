package uz.gita.fooddeliverydemo.domain.useCase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import uz.gita.fooddeliverydemo.data.foodData.AdsData
import uz.gita.fooddeliverydemo.data.foodData.FoodData
import uz.gita.fooddeliverydemo.data.foodData.RestaurantData
import uz.gita.fooddeliverydemo.data.foodData.SearchData
import uz.gita.fooddeliverydemo.domain.repository.FoodRepository
import uz.gita.fooddeliverydemo.domain.useCase.FoodUseCase
import javax.inject.Inject

class FoodUseCaseImpl @Inject constructor(
    private val foodRepository: FoodRepository
) : FoodUseCase {
    override fun getAds() = callbackFlow<Result<List<AdsData>>> {
        foodRepository.getAds(
            {
                val result = ArrayList<AdsData>()
                result.add(it.last())
                result.addAll(it)
                result.add(it.first())
                trySend(Result.success(it))
            },
            {
                trySend(Result.failure(it))
            }
        )
        awaitClose {}
    }.flowOn(Dispatchers.IO)

    override fun getRestaurants() = callbackFlow<Result<List<RestaurantData>>> {
        foodRepository.getRestaurants(
            {
                trySend(Result.success(it))
            },
            {
                trySend(Result.failure(it))
            }
        )
        awaitClose {}
    }.flowOn(Dispatchers.IO)

    override fun getFoods() = callbackFlow<Result<List<FoodData>>> {
        foodRepository.getFoods(
            {
                trySend(Result.success(it))
            },
            {
                trySend(Result.failure(it))
            }
        )
        awaitClose {}
    }.flowOn(Dispatchers.IO)

    override fun getFoodsByRestaurant(restaurants: List<RestaurantData>) = callbackFlow<Result<List<FoodData>>> {
        val selected = ArrayList<Int>()
        restaurants.forEach {
            if (it.selected)
                selected.add(it.id!!)
        }
        foodRepository.getFoodsByRestaurant(
            selected,
            {
                trySend(Result.success(it))
            },
            {
                trySend(Result.failure(it))
            }
        )
        awaitClose {}
    }.flowOn(Dispatchers.IO)

    override fun getFavourites() = callbackFlow<Result<List<Int>>> {
        foodRepository.getFavourite(
            {
                trySend(Result.success(it))
            },
            {
                trySend(Result.failure(it))
            }
        )
        awaitClose {}
    }.flowOn(Dispatchers.IO)

    override fun setFavourite(id: Int, liked: Boolean) = callbackFlow<Result<Unit>> {
        foodRepository.setFavourite(
            id,
            liked,
            {
                trySend(Result.success(Unit))
            },
            {
                trySend(Result.failure(it))
            }
        )
        awaitClose {}
    }.flowOn(Dispatchers.IO)

    override fun changeCountFood(id: Int, count: Int) = callbackFlow<Result<Unit>> {
        foodRepository.changeCountFood(
            id,
            count,
            {
                trySend(Result.success(Unit))
            },
            {
                trySend(Result.failure(it))
            }
        )
        awaitClose {}
    }.flowOn(Dispatchers.IO)

    override fun getFoodCount() = callbackFlow<Result<List<Pair<Int, Int>>>> {
        foodRepository.getFoodCount(
            {
                trySend(Result.success(it))
            },
            {
                trySend(Result.failure(it))
            }
        )
        awaitClose {}
    }.flowOn(Dispatchers.IO)

    override fun getFoodBySearch(text: String) = callbackFlow<Result<List<FoodData>>> {
        foodRepository.getFoodBySearch(
            text,
            {
                trySend(Result.success(it))
            },
            {
                trySend(Result.failure(it))
            }
        )
        awaitClose {}
    }.flowOn(Dispatchers.IO)

    override fun getFoodsSearched() = callbackFlow<Result<List<SearchData>>> {
        foodRepository.getRecentSearchedFoods(
            {
                trySend(Result.success(it.sortedByDescending { food -> food.time }))
            },
            {
                trySend(Result.failure(it))
            }
        )
        awaitClose {}
    }.flowOn(Dispatchers.IO)

    override fun addFoodToSearched(data: SearchData) = callbackFlow<Result<Unit>> {
        foodRepository.addFoodToRecentSearched(
            data,
            {
                trySend(Result.success(Unit))
            },
            {
                trySend(Result.failure(it))
            }
        )
        awaitClose {}
    }.flowOn(Dispatchers.IO)

    override fun deleteFoodFromSearched(data: SearchData) = callbackFlow<Result<Unit>> {
        foodRepository.deleteFoodFromRecentSearched(
            data,
            {
                trySend(Result.success(Unit))
            },
            {
                trySend(Result.failure(it))
            }
        )
        awaitClose {}
    }.flowOn(Dispatchers.IO)
}