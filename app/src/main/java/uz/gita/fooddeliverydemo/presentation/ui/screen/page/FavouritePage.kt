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
import uz.gita.fooddeliverydemo.databinding.PageFavouriteBinding
import uz.gita.fooddeliverydemo.presentation.ui.adapter.FavouritesAdapter
import uz.gita.fooddeliverydemo.presentation.ui.screen.MainScreenDirections
import uz.gita.fooddeliverydemo.presentation.viewModel.FavouritePageVM
import uz.gita.fooddeliverydemo.presentation.viewModel.impl.FavouritePageVMImpl

@AndroidEntryPoint
class FavouritePage : Fragment(R.layout.page_favourite) {
    private val viewModel: FavouritePageVM by viewModels<FavouritePageVMImpl>()
    private val binding by viewBinding(PageFavouriteBinding::bind)
    private val adapter = FavouritesAdapter()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {

        recyclerViewFoods.adapter = adapter
        recyclerViewFoods.layoutManager = LinearLayoutManager(requireContext())

        adapter.setDeleteListener {
            viewModel.deleteFavourite(it)
        }

        adapter.setSelectListener {
            viewModel.goDescriptionScreen(it)
        }

        adapter.setAddBasketListener {
            viewModel.addBasket(it)
        }

        viewModel.goDescriptionScreenLiveData.observe(this@FavouritePage, goDescriptionObserver)
        viewModel.loadingFavouritesLiveData.observe(viewLifecycleOwner, loadingFoodsObserver)
        viewModel.favouritesLiveData.observe(viewLifecycleOwner, foodObserver)
        viewModel.errorMessageLiveData.observe(viewLifecycleOwner, errorMessageObserver)
        viewModel.positiveMessageLiveData.observe(viewLifecycleOwner, positiveMessageObserver)
    }

    private val goDescriptionObserver = Observer<FoodData> {
        findNavController().navigate(MainScreenDirections.actionMainScreenToDescriptionScreen(it))
    }

    private val loadingFoodsObserver = Observer<Boolean> {
        if (it) {
            binding.recyclerViewFoods.visibility = View.GONE
            binding.shimmerFavourites.visibility = View.VISIBLE
            binding.shimmerFavourites.startShimmerAnimation()
        } else {
            binding.shimmerFavourites.stopShimmerAnimation()
            binding.shimmerFavourites.visibility = View.GONE
            binding.recyclerViewFoods.visibility = View.VISIBLE
        }
    }

    private val foodObserver = Observer<List<FoodData>> {
        adapter.submitList(it)
    }

    private val errorMessageObserver = Observer<String> {
        //todo
    }

    private val positiveMessageObserver = Observer<String> {
        //todo
    }
}