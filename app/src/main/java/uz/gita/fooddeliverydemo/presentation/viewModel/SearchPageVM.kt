package uz.gita.fooddeliverydemo.presentation.viewModel

import androidx.lifecycle.LiveData
import uz.gita.fooddeliverydemo.data.enum.SortType
import uz.gita.fooddeliverydemo.data.foodData.FoodData
import uz.gita.fooddeliverydemo.data.foodData.SearchData

interface SearchPageVM {
    val foodsLiveData: LiveData<List<FoodData>>
    val loadingLiveData: LiveData<Boolean>
    val recentSearchedLiveData: LiveData<List<SearchData>>
    val errorMessageLiveData: LiveData<String>
    val goDescriptionLiveData: LiveData<FoodData>
    val clearLiveData: LiveData<Unit>
    val isSearchingLiveData: LiveData<Boolean>

    fun loadFoods()
    fun loadFavourites(foods: List<FoodData>)
    fun loadCounts(foods: List<FoodData>)
    fun sortByType(type: SortType)
    fun clear()
    fun goDescription(food: FoodData)
    fun foodBySearch(text: String)
    fun setFavourite(id: Int, favourite: Boolean)
    fun addBasket(id: Int)
    fun addToSearched(data: SearchData)
    fun deleteFromSearched(data: SearchData)
}