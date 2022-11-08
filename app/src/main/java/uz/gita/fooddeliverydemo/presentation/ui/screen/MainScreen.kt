package uz.gita.fooddeliverydemo.presentation.ui.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.fooddeliverydemo.R
import uz.gita.fooddeliverydemo.databinding.ScreenMainBinding
import uz.gita.fooddeliverydemo.presentation.ui.adapter.MainScreenAdapter

class MainScreen : Fragment(R.layout.screen_main) {

    private val binding by viewBinding(ScreenMainBinding::bind)
    private lateinit var adapter: MainScreenAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {

        viewPager.isUserInputEnabled = false
        adapter = MainScreenAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> viewPager.setCurrentItem(0, false)
                R.id.search -> viewPager.setCurrentItem(1, false)
                R.id.basket -> viewPager.setCurrentItem(2, false)
                R.id.favourite -> viewPager.setCurrentItem(3, false)
                R.id.user -> viewPager.setCurrentItem(4, false)
            }
            true
        }
    }

}