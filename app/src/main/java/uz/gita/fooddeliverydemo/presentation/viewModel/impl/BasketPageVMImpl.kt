package uz.gita.fooddeliverydemo.presentation.viewModel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.fooddeliverydemo.data.foodData.FoodData
import uz.gita.fooddeliverydemo.domain.useCase.FoodUseCase
import uz.gita.fooddeliverydemo.presentation.viewModel.BasketPageVM
import javax.inject.Inject

@HiltViewModel
class BasketPageVMImpl @Inject constructor(
    private val foodUseCase: FoodUseCase
) : ViewModel(), BasketPageVM {
    override val foodsLiveData = MutableLiveData<List<FoodData>>()
    override val errorMessageLiveData = MutableLiveData<String>()
    override val goDescriptionScreenLiveData = MutableLiveData<FoodData>()
    override val loadingLiveData = MutableLiveData<Boolean>()
    override val costLiveData = MutableLiveData<Double>()
    override val oldCountLiveData = MutableLiveData<Pair<Int, Int>>()

    init {
        loadFoods()
    }

    override fun loadFoods() {
        loadingLiveData.value = true
        foodUseCase.getFoods().onEach {
            it.onSuccess { foods ->
                loadCounts(foods)
            }
            it.onFailure { error ->
                loadingLiveData.value = false
                errorMessageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    private fun loadCounts(foods: List<FoodData>) {
        foodUseCase.getFoodCount().onEach {
            loadingLiveData.value = false
            it.onSuccess { counts ->
                var sum = 0.0
                var index = 0
                val temp = ArrayList<FoodData>()
                foods.forEach { food ->
                    if (index < counts.size && food.id == counts[index].first) {
                        food.count = counts[index].second
                        temp.add(food)
                        sum += counts[index].second * food.cost!!
                        index++
                    } else return@forEach
                }

                costLiveData.value = sum
                foodsLiveData.value = temp
            }
            it.onFailure { error ->
                errorMessageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun changeCountFood(id: Int, count: Int) {
        val temp = foodsLiveData.value
        var sum = costLiveData.value ?: 0.0
        var oldSum = costLiveData.value ?: 0.0
        var oldCount = 0
        temp?.forEach { food ->
            if (food.id == id) {
                oldCount = food.count
                sum += (count - food.count) * food.cost!!
                food.count = count
                return@forEach
            }
        }
        costLiveData.value = sum
        foodUseCase.changeCountFood(id, count).onEach {
            it.onFailure { error ->
                costLiveData.value = oldSum
                oldCountLiveData.value = Pair(id, oldCount)
                errorMessageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun deleteFoodFromBasket(id: Int) {
        val temp = foodsLiveData.value as MutableList
        for (i in temp.indices) {
            if (temp[i].id == id) {
                temp.removeAt(i)
                break
            }
        }
        foodsLiveData.value = temp
        foodUseCase.changeCountFood(id, 0).onEach {
            it.onFailure { error ->
                errorMessageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun goDescriptionScreen(food: FoodData) {
        goDescriptionScreenLiveData.value = food
    }
}