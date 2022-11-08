package uz.gita.fooddeliverydemo.presentation.viewModel.impl

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.fooddeliverydemo.data.enum.SortType
import uz.gita.fooddeliverydemo.data.foodData.FoodData
import uz.gita.fooddeliverydemo.data.foodData.SearchData
import uz.gita.fooddeliverydemo.domain.useCase.FoodUseCase
import uz.gita.fooddeliverydemo.presentation.viewModel.SearchPageVM
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SearchPageVMImpl @Inject constructor(
    private val foodUseCase: FoodUseCase
) : ViewModel(), SearchPageVM {
    override val foodsLiveData = MutableLiveData<List<FoodData>>()
    override val loadingLiveData = MutableLiveData<Boolean>()
    override val recentSearchedLiveData = MutableLiveData<List<SearchData>>()
    override val errorMessageLiveData = MutableLiveData<String>()
    override val goDescriptionLiveData = MutableLiveData<FoodData>()
    override val clearLiveData = MutableLiveData<Unit>()
    override val isSearchingLiveData = MutableLiveData<Boolean>()

    private var currentSortType = SortType.LOW_HIGH

    init {
        foodBySearch("")
    }

    override fun loadFoods() {
        loadingLiveData.value = true
        foodUseCase.getFoods().onEach {
            it.onSuccess { foods ->
                loadFavourites(foods)
            }
            it.onFailure { error ->
                loadingLiveData.value = false
                errorMessageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun loadFavourites(foods: List<FoodData>) {
        foodUseCase.getFavourites().onEach {
            it.onSuccess { favourites ->
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
                        } else return@forEach
                    }
                }
                loadCounts(foods)
            }
            it.onFailure { error ->
                loadingLiveData.value = false
                errorMessageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun loadCounts(foods: List<FoodData>) {
        foodUseCase.getFoodCount().onEach {
            loadingLiveData.value = false
            it.onSuccess { counts ->
                var index = 0
                foods.forEach { food ->
                    if (index < counts.size && food.id == counts[index].first) {
                        food.count = counts[index].second
                        index++
                    } else return@forEach
                }
                foodsLiveData.value = foods
                sortByType(currentSortType)
            }
            it.onFailure { error ->
                errorMessageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun sortByType(type: SortType) {
        currentSortType = type
        foodsLiveData.value?.let {
            foodsLiveData.value = when (type) {
                SortType.LOW_HIGH -> it.sortedBy { food -> food.cost }
                SortType.HIGH_LOW -> it.sortedByDescending { food -> food.cost }
                SortType.NAME_INCREASE -> it.sortedBy { food -> food.name }
                SortType.NAME_DECREASE -> it.sortedByDescending { food -> food.name }
                else -> it.sortedBy { food -> food.restaurant_id }
            }
        }
    }

    override fun clear() {
        clearLiveData.value = Unit
    }

    override fun goDescription(food: FoodData) {
        goDescriptionLiveData.value = food
    }

    override fun foodBySearch(text: String) {
        if (text.isEmpty()) {
            isSearchingLiveData.value = false
            foodUseCase.getFoodsSearched().onEach {
                it.onSuccess { foods ->
                    recentSearchedLiveData.value = foods
                }
                it.onFailure { error ->
                    errorMessageLiveData.value = error.message
                }
            }.launchIn(viewModelScope)
        } else {
            loadingLiveData.value = true
            isSearchingLiveData.value = true
            foodUseCase.getFoodBySearch(text).onEach {
                it.onSuccess { foods ->
                    loadFavourites(foods)
                }
                it.onFailure { error ->
                    loadingLiveData.value = false
                    errorMessageLiveData.value = error.message
                }
            }.launchIn(viewModelScope)
        }
    }

    override fun setFavourite(id: Int, favourite: Boolean) {
        foodUseCase.setFavourite(id, favourite).onEach {
            it.onFailure { error ->
                errorMessageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun addBasket(id: Int) {
        foodUseCase.changeCountFood(id, 1).onEach {
            it.onFailure { error ->
                errorMessageLiveData.value = error.message
            }
        }
    }

    override fun addToSearched(data: SearchData) {
        foodUseCase.addFoodToSearched(data).onEach {
            it.onSuccess {
                Log.d("TTT", "successful")
            }
            it.onFailure { error ->
                errorMessageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun deleteFromSearched(data: SearchData) {
        foodBySearch("")
        foodUseCase.deleteFoodFromSearched(data).onEach {
            it.onFailure { error ->
                errorMessageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

}