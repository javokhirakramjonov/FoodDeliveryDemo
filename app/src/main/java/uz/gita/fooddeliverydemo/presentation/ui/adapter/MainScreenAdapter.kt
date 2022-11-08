package uz.gita.fooddeliverydemo.presentation.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.gita.fooddeliverydemo.presentation.ui.screen.page.*

class MainScreenAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter
    (fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomePage()
            1 -> SearchPage()
            2 -> BasketPage()
            3 -> FavouritePage()
            else -> UserPage()
        }
    }

}