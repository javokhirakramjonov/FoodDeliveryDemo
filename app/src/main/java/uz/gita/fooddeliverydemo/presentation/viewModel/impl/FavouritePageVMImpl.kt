package uz.gita.fooddeliverydemo.presentation.viewModel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.fooddeliverydemo.data.foodData.FoodData
import uz.gita.fooddeliverydemo.domain.useCase.FoodUseCase
import uz.gita.fooddeliverydemo.presentation.viewModel.FavouritePageVM
import javax.inject.Inject

@HiltViewModel
class FavouritePageVMImpl @Inject constructor(
    private val foodUseCase: FoodUseCase
) : ViewModel(), FavouritePageVM {
    override val favouritesLiveData = MutableLiveData<List<FoodData>>()
    override val errorMessageLiveData = MutableLiveData<String>()
    override val positiveMessageLiveData = MutableLiveData<String>()
    override val loadingFavouritesLiveData = MutableLiveData<Boolean>()
    override val goDescriptionScreenLiveData = MutableLiveData<FoodData>()

    init {
        loadFoods()
    }

    override fun loadFoods() {
        loadingFavouritesLiveData.value = true
        foodUseCase.getFoods().onEach {
            it.onSuccess { foods ->
                loadFavourites(foods)
            }
            it.onFailure { error ->
                loadingFavouritesLiveData.value = false
                errorMessageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun loadFavourites(foods: List<FoodData>) {
        foodUseCase.getFavourites().onEach {
            it.onSuccess { favourites ->
                val list = ArrayList<FoodData>()
                foods.forEach { food ->
                    food.favourite = false
                    food.count = 0
                }
                if (favourites.isNotEmpty()) {
                    var index = 0
                    foods.forEach { food ->
                        if (index < favourites.size && food.id == favourites[index]) {
                            food.favourite = true
                            index++
                            list.add(food)
                        } else
                            return@forEach
                    }
                }
                loadFoodCount(list)
            }
            it.onFailure { error ->
                errorMessageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun loadFoodCount(foods: List<FoodData>) {
        foodUseCase.getFoodCount().onEach {
            loadingFavouritesLiveData.value = false
            it.onSuccess { counts ->
                var index = 0
                foods.forEach { food ->
                    if (index < counts.size && food.id == counts[index].first) {
                        food.count = counts[index].second
                        index++
                    } else
                        food.count = 0
                }

                favouritesLiveData.value = foods
            }
            it.onFailure { error ->
                errorMessageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun addBasket(id: Int) {
        val temp = favouritesLiveData.value as MutableList
        for (i in temp.indices) {
            if (temp[i].id == id) {
                temp[i].count = 1
                break
            }
        }
        favouritesLiveData.value = temp
        foodUseCase.changeCountFood(id, 1).onEach {
            it.onFailure { error ->
                errorMessageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun deleteFavourite(id: Int) {
        val temp = favouritesLiveData.value as MutableList
        for (i in temp.indices) {
            if (temp[i].id == id) {
                temp.removeAt(i)
                break
            }
        }
        favouritesLiveData.value = temp
        foodUseCase.setFavourite(id, false).onEach {
            it.onFailure { error ->
                errorMessageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun goDescriptionScreen(food: FoodData) {
        goDescriptionScreenLiveData.value = food
    }
}