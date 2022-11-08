package uz.gita.fooddeliverydemo.presentation.viewModel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.gita.fooddeliverydemo.data.foodData.AdsData
import uz.gita.fooddeliverydemo.data.foodData.FoodData
import uz.gita.fooddeliverydemo.data.foodData.RestaurantData
import uz.gita.fooddeliverydemo.domain.repository.LocalRepository
import uz.gita.fooddeliverydemo.domain.useCase.AuthUseCase
import uz.gita.fooddeliverydemo.domain.useCase.FoodUseCase
import uz.gita.fooddeliverydemo.presentation.viewModel.HomePageVM
import javax.inject.Inject

@HiltViewModel
class HomePageVMImpl @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val localData: LocalRepository,
    private val foodUseCase: FoodUseCase
) : ViewModel(), HomePageVM {
    override val adsLiveData = MutableLiveData<List<AdsData>>()
    override val restaurantLiveData = MutableLiveData<List<RestaurantData>>()
    override val foodsLiveData = MutableLiveData<List<FoodData>>()
    override val messageLiveData = MutableLiveData<String>()
    override val userNameLiveData = MutableLiveData<String>()
    override val loadingFoodLiveData = MutableLiveData<Boolean>()
    override val loadingRestaurantLiveData = MutableLiveData<Boolean>()
    override val adsIndexLiveData = MutableLiveData<Int>()
    override val goDescriptionScreenLiveData = MutableLiveData<FoodData>()

    private var job: Job? = null

    init {
        loadUserName()
        loadRestaurants()
    }

    override fun loadAds() {
        foodUseCase.getAds().onEach {
            it.onSuccess { ads ->
                adsLiveData.value = ads
            }
            it.onFailure { error ->
                messageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun loadRestaurants() {
        loadingRestaurantLiveData.value = true
        loadingFoodLiveData.value = true
        foodUseCase.getRestaurants().onEach {
            it.onSuccess { restaurants ->
                restaurantLiveData.value = restaurants
                loadingRestaurantLiveData.value = false
                loadFoods()
            }
            it.onFailure { error ->
                loadingFoodLiveData.value = true
                messageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun loadFoods() {
        foodUseCase.getFoods().onEach {
            it.onSuccess { foods ->
                if (foods.isEmpty()) {
                    loadingFoodLiveData.value = false
                    foodsLiveData.value = foods
                } else {
                    loadFavouriteFood(foods)
                }
            }
            it.onFailure { error ->
                loadingFoodLiveData.value = false
                messageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun startAds(position: Int) {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            delay(2000)
            adsIndexLiveData.postValue(position + 1)
        }

    }

    override fun selectUnselectRestaurant(restaurants: List<RestaurantData>) {
        loadingFoodLiveData.value = true
        viewModelScope.launch(Dispatchers.IO) {
            var selectedRestaurantCount = 0
            restaurants.forEach {
                if (it.selected)
                    selectedRestaurantCount++
            }
            if (selectedRestaurantCount in 1 until restaurants.size) {
                foodUseCase.getFoodsByRestaurant(restaurants).onEach {
                    it.onSuccess { foods ->
                        loadFavouriteFood(foods)
                    }
                    it.onFailure { error ->
                        loadingFoodLiveData.value = false
                        messageLiveData.value = error.message
                    }
                }.launchIn(viewModelScope)
            } else
                loadFoods()
        }
    }

    override fun setFavouriteFood(food: FoodData) {
        foodUseCase.setFavourite(food.id!!, food.favourite).onEach {
            it.onFailure { error ->
                messageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun loadFavouriteFood(foods: List<FoodData>) {
        foodUseCase.getFavourites().onEach {
            foods.forEach { food ->
                food.favourite = false
                food.count = 0
            }
            it.onSuccess { favourites ->
                if (favourites.isNotEmpty()) {
                    var index = 0
                    foods.forEach { food ->
                        if (index < favourites.size && food.id == favourites[index]) {
                            food.favourite = true
                            index++
                        } else return@forEach
                    }
                }
                loadFoodCount(foods)
            }
            it.onFailure { error ->
                loadingFoodLiveData.value = false
                messageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun loadFoodCount(foods: List<FoodData>) {
        foodUseCase.getFoodCount().onEach {
            loadingFoodLiveData.value = false
            it.onSuccess { counts ->
                var index = 0
                foods.forEach { food ->
                    if (index < counts.size && food.id == counts[index].first) {
                        food.count = counts[index].second
                        index++
                    } else return@forEach
                }
                foodsLiveData.value = fixFoods(foods)
            }
            it.onFailure { error ->
                messageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    private fun fixFoods(foods: List<FoodData>): List<FoodData> {
        val foodsFull = foods.sortedBy { food -> food.restaurant_id } as MutableList
        foodsFull.forEach { food ->
            food.restaurant_name = restaurantLiveData.value!![food.restaurant_id!! - 1].name
        }

        //avoid from odd elements
        foodsFull[0].sub_id = 1
        var count = 1
        var deleted = 0
        for (i in 1 until foods.size) {
            if (foods[i].restaurant_id!! != foods[i - 1].restaurant_id!!) {
                if (count % 2 == 1) {
                    foodsFull.add(i + deleted++, FoodData(-1))
                }
                count = 1
            } else count++
            foodsFull[i + deleted].sub_id = count
        }
        if (count % 2 == 1)
            foodsFull.add(FoodData(-1))
        return foodsFull
    }

    override fun loadUserName() {
        authUseCase.getUsername(localData.getUser()).onEach {
            it.onSuccess { name ->
                userNameLiveData.value = name
            }
        }.launchIn(viewModelScope)
    }

    override fun refreshFavourites() {
        if (foodsLiveData.value != null) {
            loadFavouriteFood(foodsLiveData.value!!)
        }
    }

    override fun goDescriptionScreen(food: FoodData) {
        goDescriptionScreenLiveData.value = food
    }

    override fun addBasket(id: Int) {
        val temp = foodsLiveData.value as MutableList
        for (i in temp.indices) {
            if (temp[i].id == id) {
                temp[i].count = 1
                break
            }
        }
        foodsLiveData.value = temp
        foodUseCase.changeCountFood(id, 1).onEach {
            it.onFailure { error ->
                messageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }
}