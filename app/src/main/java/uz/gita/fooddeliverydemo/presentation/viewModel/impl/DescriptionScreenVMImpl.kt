package uz.gita.fooddeliverydemo.presentation.viewModel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.fooddeliverydemo.domain.useCase.FoodUseCase
import uz.gita.fooddeliverydemo.presentation.viewModel.DescriptionScreenVM
import javax.inject.Inject

@HiltViewModel
class DescriptionScreenVMImpl @Inject constructor(
    private val foodUseCase: FoodUseCase
) : ViewModel(), DescriptionScreenVM {
    override val goBackLiveData = MutableLiveData<Unit>()
    override val countLiveData = MutableLiveData<Int>()
    override val subTotalLiveData = MutableLiveData<String>()
    override val errorMessageLiveData = MutableLiveData<String>()
    override val addBasketStateLiveData = MutableLiveData<Boolean>()
    override val decrementStateLiveData = MutableLiveData<Boolean>()
    override val counterButtonsStateLiveData = MutableLiveData<Boolean>()

    override fun goBack() {
        goBackLiveData.value = Unit
    }

    override fun addToBasket(id: Int, count: Int, cost: Double, oldCount: Int, state: Boolean) {
        loader(count, cost, state)
        foodUseCase.changeCountFood(id, count).onEach {
            it.onFailure { error ->
                loader(oldCount, cost, true)
                errorMessageLiveData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun loader(count: Int, cost: Double, state: Boolean) {
        if(state)
            countLiveData.value = count
        subTotalLiveData.value = "$ " + (count * cost).toString()
        if (count == 0) {
            counterButtonsStateLiveData.value = false
            addBasketStateLiveData.value = true
        } else {
            counterButtonsStateLiveData.value = true
            addBasketStateLiveData.value = false
        }
    }
}