package uz.gita.fooddeliverydemo.presentation.ui.screen.page

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.fooddeliverydemo.R
import uz.gita.fooddeliverydemo.data.foodData.AdsData
import uz.gita.fooddeliverydemo.data.foodData.FoodData
import uz.gita.fooddeliverydemo.data.foodData.RestaurantData
import uz.gita.fooddeliverydemo.databinding.PageHomeBinding
import uz.gita.fooddeliverydemo.presentation.ui.adapter.FoodsAdapter
import uz.gita.fooddeliverydemo.presentation.ui.adapter.RestaurantsAdapter
import uz.gita.fooddeliverydemo.presentation.ui.screen.MainScreenDirections
import uz.gita.fooddeliverydemo.presentation.viewModel.HomePageVM
import uz.gita.fooddeliverydemo.presentation.viewModel.impl.HomePageVMImpl
import uz.gita.fooddeliverydemo.utils.snackErrorMessage

@AndroidEntryPoint
class HomePage : Fragment(R.layout.page_home) {

    private val viewModel: HomePageVM by viewModels<HomePageVMImpl>()
    private val binding by viewBinding(PageHomeBinding::bind)
    private val resAdapter = RestaurantsAdapter()
    private val foodAdapter = FoodsAdapter()

    @SuppressLint("SetTextI18n", "FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {

        recyclerViewRestaurants.adapter = resAdapter
        recyclerViewRestaurants.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL,
            false
        )
        resAdapter.setRestaurantSelectedListener {
            viewModel.selectUnselectRestaurant(it)
        }

        recyclerViewFoods.adapter = foodAdapter
        recyclerViewFoods.layoutManager = GridLayoutManager(requireContext(), 2)

        foodAdapter.setAddBasketListener {
            viewModel.addBasket(it)
        }
        foodAdapter.setClickFavouriteListener {
            viewModel.setFavouriteFood(it)
        }
        foodAdapter.setSelectListener {
            viewModel.goDescriptionScreen(it)
        }

        viewModel.goDescriptionScreenLiveData.observe(this@HomePage, goDescriptionScreenObserver)
        viewModel.userNameLiveData.observe(viewLifecycleOwner, userNameObserver)
        viewModel.adsIndexLiveData.observe(viewLifecycleOwner, loadAdsObserver)
        viewModel.loadingFoodLiveData.observe(viewLifecycleOwner, loadingFoodObserver)
        viewModel.loadingRestaurantLiveData.observe(viewLifecycleOwner, loadingRestaurantObserver)
        viewModel.foodsLiveData.observe(viewLifecycleOwner, foodObserver)
        viewModel.adsLiveData.observe(viewLifecycleOwner, adsObserver)
        viewModel.restaurantLiveData.observe(viewLifecycleOwner, restaurantsObserver)
        viewModel.messageLiveData.observe(viewLifecycleOwner, messageObserver)
    }

    private val goDescriptionScreenObserver = Observer<FoodData> {
        findNavController().navigate(MainScreenDirections.actionMainScreenToDescriptionScreen(it))
    }

    private val userNameObserver = Observer<String> {
        binding.textUserName.text = it
    }

    private val loadAdsObserver = Observer<Int> {
        binding.viewPagerAds.setCurrentItem(it, true)
    }

    private val loadingFoodObserver = Observer<Boolean> {
        if (it) {
            binding.recyclerViewFoods.visibility = View.GONE
            binding.shimmerAnimationFoods.visibility = View.VISIBLE
            binding.shimmerAnimationFoods.startShimmerAnimation()
        } else {
            binding.recyclerViewFoods.visibility = View.VISIBLE
            binding.shimmerAnimationFoods.visibility = View.GONE
            binding.shimmerAnimationFoods.stopShimmerAnimation()
        }
    }

    private val loadingRestaurantObserver = Observer<Boolean> {
        if (it) {
            binding.recyclerViewRestaurants.visibility = View.GONE
            binding.shimmerAnimationRestaurants.visibility = View.VISIBLE
            binding.shimmerAnimationRestaurants.startShimmerAnimation()
        } else {
            binding.recyclerViewRestaurants.visibility = View.VISIBLE
            binding.shimmerAnimationRestaurants.visibility = View.GONE
            binding.shimmerAnimationRestaurants.stopShimmerAnimation()
        }
    }

    private val foodObserver = Observer<List<FoodData>> {
        foodAdapter.submitList(it)
    }

    private val adsObserver = Observer<List<AdsData>> {
        //todo
    }

    private val restaurantsObserver = Observer<List<RestaurantData>> {
        resAdapter.submitList(it)
    }

    private val messageObserver = Observer<String> {
        snackErrorMessage(it)
    }

}