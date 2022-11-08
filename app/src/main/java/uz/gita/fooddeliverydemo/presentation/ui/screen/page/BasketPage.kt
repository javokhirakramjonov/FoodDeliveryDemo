package uz.gita.fooddeliverydemo.presentation.ui.screen.page

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.fooddeliverydemo.R
import uz.gita.fooddeliverydemo.data.foodData.FoodData
import uz.gita.fooddeliverydemo.databinding.PageBasketBinding
import uz.gita.fooddeliverydemo.presentation.ui.adapter.BasketAdapter
import uz.gita.fooddeliverydemo.presentation.ui.screen.MainScreenDirections
import uz.gita.fooddeliverydemo.presentation.viewModel.BasketPageVM
import uz.gita.fooddeliverydemo.presentation.viewModel.impl.BasketPageVMImpl
import uz.gita.fooddeliverydemo.utils.snackErrorMessage

@AndroidEntryPoint
class BasketPage : Fragment(R.layout.page_basket) {

    private val viewModel: BasketPageVM by viewModels<BasketPageVMImpl>()
    private val binding by viewBinding(PageBasketBinding::bind)
    private val adapter = BasketAdapter()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        binding.loader.hide()

        adapter.apply {
            setChangeCountListener { id, count ->
                viewModel.changeCountFood(id, count)
            }
            setDeleteFoodListener {
                viewModel.deleteFoodFromBasket(it)
            }
            setGetDescriptionListener {
                viewModel.goDescriptionScreen(it)
            }
        }
        recyclerViewFoods.adapter = adapter
        recyclerViewFoods.layoutManager = LinearLayoutManager(binding.root.context)

        viewModel.oldCountLiveData.observe(viewLifecycleOwner, oldCountObserver)
        viewModel.loadingLiveData.observe(viewLifecycleOwner, loadingObserver)
        viewModel.goDescriptionScreenLiveData.observe(this@BasketPage, descriptionObserver)
        viewModel.costLiveData.observe(viewLifecycleOwner, costObserver)
        viewModel.foodsLiveData.observe(viewLifecycleOwner, foodsObserver)
        viewModel.errorMessageLiveData.observe(viewLifecycleOwner, errorMessageObserver)
    }

    private val oldCountObserver = Observer<Pair<Int, Int>> {
        adapter.setOldCount(it)
    }

    private val loadingObserver = Observer<Boolean> {
        if (it)
            binding.loader.show()
        else
            binding.loader.hide()
    }

    private val descriptionObserver = Observer<FoodData> {
        findNavController().navigate(MainScreenDirections.actionMainScreenToDescriptionScreen(it))
    }

    @SuppressLint("SetTextI18n")
    private val costObserver = Observer<Double> {
        binding.textSumTotal.text = "$ $it"
    }

    private val foodsObserver = Observer<List<FoodData>> {
        adapter.submitList(it)
    }

    private val errorMessageObserver = Observer<String> {
        snackErrorMessage(it)
    }
}