package uz.gita.fooddeliverydemo.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.gita.fooddeliverydemo.R
import uz.gita.fooddeliverydemo.databinding.ItemBoardingScreenBinding

class OnBoardingScreenPagerAdapter : RecyclerView.Adapter<OnBoardingScreenPagerAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemBoardingScreenBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            when (absoluteAdapterPosition) {
                0 -> {
                    binding.imageView.setImageResource(R.drawable.splash1)
                    binding.title.setText(R.string.text_onboard1_title1)
                    binding.description.setText(R.string.text_onboard1_title2)
                }
                1 -> {
                    binding.imageView.setImageResource(R.drawable.splash2)
                    binding.title.setText(R.string.text_onboard2_title1)
                    binding.description.setText(R.string.text_onboard2_title2)
                }
                else -> {
                    binding.imageView.setImageResource(R.drawable.splash3)
                    binding.title.setText(R.string.text_onboard3_title1)
                    binding.description.setText(R.string.text_onboard3_title2)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingScreenPagerAdapter.ViewHolder {
        return ViewHolder(ItemBoardingScreenBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: OnBoardingScreenPagerAdapter.ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return 3
    }
}