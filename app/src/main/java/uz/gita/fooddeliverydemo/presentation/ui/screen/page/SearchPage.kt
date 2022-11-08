package uz.gita.fooddeliverydemo.presentation.ui.screen.page

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.fooddeliverydemo.R
import uz.gita.fooddeliverydemo.data.enum.SortType
import uz.gita.fooddeliverydemo.data.foodData.FoodData
import uz.gita.fooddeliverydemo.data.foodData.SearchData
import uz.gita.fooddeliverydemo.databinding.PageSearchBinding
import uz.gita.fooddeliverydemo.presentation.ui.adapter.RecentFoodAdapter
import uz.gita.fooddeliverydemo.presentation.ui.adapter.SearchAdapter
import uz.gita.fooddeliverydemo.presentation.ui.screen.MainScreenDirections
import uz.gita.fooddeliverydemo.presentation.viewModel.SearchPageVM
import uz.gita.fooddeliverydemo.presentation.viewModel.impl.SearchPageVMImpl
import uz.gita.fooddeliverydemo.utils.hideKeyboard
import uz.gita.fooddeliverydemo.utils.snackErrorMessage
import java.util.*

@AndroidEntryPoint
class SearchPage : Fragment(R.layout.page_search) {

    private val viewModel: SearchPageVM by viewModels<SearchPageVMImpl>()
    private val binding by viewBinding(PageSearchBinding::bind)
    private val adapter = SearchAdapter()
    private val adapterRecent = RecentFoodAdapter()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {

        loader.hide()

        recentFoods.adapter = adapterRecent
        recentFoods.layoutManager = LinearLayoutManager(requireContext())
        adapterRecent.apply {
            setSelectListener {
                searchView.setText(it.text)
                viewModel.foodBySearch(it.text?:"")
            }
            setDeleteListener {
                viewModel.deleteFromSearched(it)
            }
        }

        foods.adapter = adapter
        foods.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter.apply {
            setAddBasketListener {
                viewModel.addBasket(it)
            }
            setClickFavouriteListener {
                viewModel.setFavourite(it.id!!, it.favourite)
            }
            setSelectListener {
                viewModel.goDescription(it)
            }
        }

        searchView.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.addToSearched(SearchData(Date().toString(), searchView.text.toString()))
                hideKeyboard()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        clearWord.setOnClickListener {
            viewModel.clear()
        }

        searchView.addTextChangedListener {
            it?.let {
                viewModel.foodBySearch(it.toString())
                foods.scrollToPosition(0)
            }
        }

        buttonSort.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(requireContext(), buttonSort)
            popupMenu.menuInflater.inflate(R.menu.menu_sort_type, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.lowHigh -> {
                        typeSort.setText(R.string.low_high_cost)
                        viewModel.sortByType(SortType.LOW_HIGH)
                    }
                    R.id.highLow -> {
                        typeSort.setText(R.string.high_low_cost)
                        viewModel.sortByType(SortType.HIGH_LOW)
                    }
                    R.id.lowHighName -> {
                        typeSort.setText(R.string.a_z_name)
                        viewModel.sortByType(SortType.NAME_INCREASE)
                    }
                    R.id.highLowName -> {
                        typeSort.setText(R.string.z_a_name)
                        viewModel.sortByType(SortType.NAME_DECREASE)
                    }
                    R.id.lowHighRestaurant -> {
                        typeSort.setText(R.string.by_restaurant)
                        viewModel.sortByType(SortType.RESTAURANT)
                    }
                }
                foods.scrollToPosition(0)
                true
            })
            popupMenu.show()
        }

        viewModel.isSearchingLiveData.observe(viewLifecycleOwner, isSearchingObserver)
        viewModel.loadingLiveData.observe(viewLifecycleOwner, loadingObserver)
        viewModel.recentSearchedLiveData.observe(viewLifecycleOwner, recentFoodsObserver)
        viewModel.goDescriptionLiveData.observe(this@SearchPage, goDescriptionObserver)
        viewModel.errorMessageLiveData.observe(viewLifecycleOwner, errorMessageObserver)
        viewModel.clearLiveData.observe(viewLifecycleOwner, clearObserver)
        viewModel.foodsLiveData.observe(viewLifecycleOwner, foodsObserver)
    }

    private val isSearchingObserver = Observer<Boolean> {
        binding.apply {
            if (it) {
                txtRecent.text = "Foods"
                recentFoods.visibility = View.GONE
                buttonSort.visibility = View.VISIBLE
                typeSort.visibility = View.VISIBLE
                foods.visibility = View.VISIBLE
            } else {
                buttonSort.visibility = View.GONE
                typeSort.visibility = View.GONE
                txtRecent.text = "Recents"
                foods.visibility = View.GONE
                recentFoods.visibility = View.VISIBLE
            }
        }
    }

    private val loadingObserver = Observer<Boolean> {
        if (it)
            binding.loader.show()
        else
            binding.loader.hide()
    }

    private val recentFoodsObserver = Observer<List<SearchData>> {
        adapterRecent.submitList(it)
    }

    private val goDescriptionObserver = Observer<FoodData> {
        findNavController().navigate(MainScreenDirections.actionMainScreenToDescriptionScreen(it))
    }

    private val errorMessageObserver = Observer<String> {
        snackErrorMessage(it)
    }

    private val clearObserver = Observer<Unit> {
        binding.searchView.setText("")
    }

    private val foodsObserver = Observer<List<FoodData>> {
        adapter.submitList(it)
    }
}