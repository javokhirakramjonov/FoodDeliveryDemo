package uz.gita.fooddeliverydemo.presentation.ui.screen

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.fooddeliverydemo.R
import uz.gita.fooddeliverydemo.data.foodData.FoodData
import uz.gita.fooddeliverydemo.databinding.ScreenDescriptionBinding
import uz.gita.fooddeliverydemo.presentation.viewModel.DescriptionScreenVM
import uz.gita.fooddeliverydemo.presentation.viewModel.impl.DescriptionScreenVMImpl
import uz.gita.fooddeliverydemo.utils.hideKeyboard
import uz.gita.fooddeliverydemo.utils.snackErrorMessage

@AndroidEntryPoint
class DescriptionScreen : Fragment(R.layout.screen_description) {

    private val viewModel: DescriptionScreenVM by viewModels<DescriptionScreenVMImpl>()
    private val binding by viewBinding(ScreenDescriptionBinding::bind)
    private val args: DescriptionScreenArgs by navArgs()
    private lateinit var food: FoodData

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        food = args.food

        Glide.with(requireContext())
            .load(Uri.parse(food.image))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.test_food)
            .error(R.drawable.test_food)
            .into(imageFood)

        buttonBack.setOnClickListener {
            viewModel.goBack()
        }

        textNameFood.text = food.name
        textDescription.text = food.description

        viewModel.loader(food.count, food.cost!!, true)

        buttonMinus.setOnClickListener {
            viewModel.addToBasket(food.id!!, food.count - 1, food.cost!!, food.count, true)
        }
        buttonPlus.isEnabled = true
        buttonPlus.setOnClickListener {
            viewModel.addToBasket(food.id!!, food.count + 1, food.cost!!, food.count, true)
        }

        textQuantity.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val temp = textQuantity.text.toString()
                if (temp.isNotEmpty() && temp != "0" && temp != "00" && temp != "000") {
                    textQuantity.setText("0")
                    viewModel.addToBasket(food.id!!, temp.toInt(), food.cost!!, food.count, false)
                } else {
                    viewModel.addToBasket(food.id!!, 0, food.cost!!, food.count, true)
                }
                hideKeyboard()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        buttonAddToBasket.setOnClickListener {
            viewModel.addToBasket(food.id!!, food.count + 1, food.cost!!, food.count, true)
        }

        viewModel.counterButtonsStateLiveData.observe(this@DescriptionScreen, counterButtonsStateObserver)
        viewModel.decrementStateLiveData.observe(this@DescriptionScreen, decrementButtonStateObserver)
        viewModel.goBackLiveData.observe(this@DescriptionScreen, goBackObserver)
        viewModel.errorMessageLiveData.observe(this@DescriptionScreen, errorMessageObserver)
        viewModel.addBasketStateLiveData.observe(this@DescriptionScreen, basketButtonStateObserver)
        viewModel.subTotalLiveData.observe(this@DescriptionScreen, subTotalObserver)
        viewModel.countLiveData.observe(this@DescriptionScreen, countObserver)
    }

    private val counterButtonsStateObserver = Observer<Boolean> {
        binding.apply {
            buttonMinus.isEnabled = it
            buttonPlus.isEnabled = it
        }
    }

    private val decrementButtonStateObserver = Observer<Boolean> {
        binding.buttonMinus.isEnabled = it
    }

    private val goBackObserver = Observer<Unit> {
        findNavController().navigateUp()
    }

    private val errorMessageObserver = Observer<String> {
        snackErrorMessage(it)
    }

    private val basketButtonStateObserver = Observer<Boolean> {
        binding.buttonAddToBasket.isEnabled = it
    }

    private val subTotalObserver = Observer<String> {
        binding.textSubTotal.text = it
    }

    private val countObserver = Observer<Int> {
        binding.textQuantity.setText(it.toString())
    }
}